import {
  assertCmdOk,
  assertCmdSuccess,
  createGatewayClient,
  OusHttpClient,
  pollUntil,
  type AholoClientConfig,
  type AholoGatewayClient,
  type CmdEnvelope,
} from '@manycore/aholo-sdk-core';

import { md5Hex } from './md5.js';
import {
  OUS_STATUS_SUCCESS,
  OUS_TERMINAL_FAILURE_STATUSES,
  type OusBlockInitData,
  type OusUploadStatusData,
  type UploadOptions,
  type UploadResult,
  type UploadToken,
} from './types.js';

function bufferToBlob(data: Buffer): Blob {
  const copy = new Uint8Array(data.length);
  copy.set(data);
  return new Blob([copy]);
}

const DEFAULT_PART_CONCURRENCY = 2;
const DEFAULT_POLL_INTERVAL_MS = 300;
const DEFAULT_POLL_TIMEOUT_MS = 300_000;
const DEFAULT_UPLOAD_PART_TIMEOUT_MS = 120_000; // 2 min per block; 1MB should upload in seconds, this covers server-side slow responses

function tokenPath(region?: AholoClientConfig['region']): string {
  return region === 'com' ? '/global/asset/v1/token' : '/asset/v1/token';
}

/**
 * Parse OUS lackBlocks entries into a Set of 1-based block numbers.
 * Entries can be individual numbers ("5") or inclusive ranges ("72-423").
 */
function parseLackBlocks(lackBlocks: string[]): Set<number> {
  const result = new Set<number>();
  for (const item of lackBlocks) {
    const dash = item.indexOf('-');
    if (dash !== -1) {
      const start = parseInt(item.slice(0, dash), 10);
      const end = parseInt(item.slice(dash + 1), 10);
      for (let i = start; i <= end; i++) result.add(i);
    } else {
      result.add(parseInt(item, 10));
    }
  }
  return result;
}

function sliceParts(data: Buffer, blockSize: number): Buffer[] {
  const parts: Buffer[] = [];
  for (let offset = 0; offset < data.length; offset += blockSize) {
    parts.push(data.subarray(offset, offset + blockSize));
  }
  return parts;
}

async function runWithConcurrency<T>(
  items: T[],
  concurrency: number,
  worker: (item: T, index: number) => Promise<void>,
): Promise<void> {
  if (items.length === 0) return;
  let nextIndex = 0;

  async function runWorker(): Promise<void> {
    while (nextIndex < items.length) {
      const index = nextIndex++;
      await worker(items[index], index);
    }
  }

  const workers = Array.from({ length: Math.min(concurrency, items.length) }, () => runWorker());
  await Promise.all(workers);
}

/**
 * Aholo asset upload client: gateway token + OUS single/multipart upload + status poll.
 */
export class AssetClient {
  private readonly gateway: AholoGatewayClient;
  private readonly config: AholoClientConfig;

  constructor(config: AholoClientConfig = {}) {
    this.config = config;
    this.gateway = createGatewayClient(config);
  }

  /** `GET /asset/v1/token` (or `/global/asset/v1/token` for global region). */
  getUploadToken(): Promise<UploadToken> {
    return this.gateway.gatewayRequest<UploadToken>({
      method: 'GET',
      path: tokenPath(this.config.region),
    });
  }

  /** Upload an in-memory buffer and return the public URL. */
  async uploadBuffer(data: Buffer, options: UploadOptions = {}): Promise<UploadResult> {
    const token = await this.getUploadToken();
    const ous = this.createOusClient(token);
    const md5 = md5Hex(data);
    const filename = options.filename ?? 'upload.bin';

    if (data.length <= token.blockSize) {
      await this.singleUpload(ous, data, md5, filename, options);
    } else {
      await this.blockUpload(ous, data, md5, filename, token.blockSize, options);
    }

    return this.pollUploadStatus(ous, options);
  }

  /** Read a local file and upload it. */
  async uploadFile(filePath: string, options: UploadOptions = {}): Promise<UploadResult> {
    const { readFile } = await import('node:fs/promises');
    const { basename } = await import('node:path');
    const data = await readFile(filePath);
    return this.uploadBuffer(data, {
      ...options,
      filename: options.filename ?? basename(filePath),
    });
  }

  private createOusClient(token: UploadToken): OusHttpClient {
    return new OusHttpClient({
      baseUrl: token.globalDomain,
      ousToken: token.ousToken,
      timeoutMs: this.config.timeoutMs,
      fetchImpl: this.config.fetch,
    });
  }

  private async singleUpload(
    ous: OusHttpClient,
    data: Buffer,
    md5: string,
    filename: string,
    options: UploadOptions,
  ): Promise<void> {
    const form = new FormData();
    form.append('md5', md5);
    form.append('file', bufferToBlob(data), filename);

    const body = await ous.request<CmdEnvelope<unknown>>({
      method: 'POST',
      path: '/ous/api/v2/single/upload',
      rawBody: form,
      signal: options.signal,
    });
    assertCmdOk(body, 'single upload');
    options.onProgress?.(data.length, data.length);
  }

  private async blockUpload(
    ous: OusHttpClient,
    data: Buffer,
    md5: string,
    filename: string,
    blockSize: number,
    options: UploadOptions,
  ): Promise<void> {
    const parts = sliceParts(data, blockSize);
    const initBody = await ous.request<CmdEnvelope<OusBlockInitData>>({
      method: 'POST',
      path: '/ous/api/v2/block/upload/init',
      query: {
        md5,
        blocks: parts.length,
        size: data.length,
        name: filename,
        metadata: options.metadata,
        customPrefix: options.customPrefix,
        customFilename: options.customFilename,
      },
      signal: options.signal,
    });
    const initData = assertCmdSuccess(initBody, 'block upload init');

    if (initData.deduplicated) {
      return;
    }

    // Filter to only the blocks that still need uploading.
    // lackBlocks = null/undefined means all blocks are needed (fresh upload).
    // lackBlocks = [] means nothing left to upload (all blocks already received).
    // Entries can be individual numbers ("5") or inclusive ranges ("72-423").
    type BlockEntry = { index: number; chunk: Buffer };
    const allEntries: BlockEntry[] = parts.map((chunk, index) => ({ index, chunk }));
    const lackSet = initData.lackBlocks != null ? parseLackBlocks(initData.lackBlocks) : null;
    const toUpload = lackSet != null
      ? allEntries.filter(({ index }) => lackSet.has(index + 1))
      : allEntries;

    if (toUpload.length === 0) {
      return;
    }

    let uploadedBytes = 0;
    const totalBytes = data.length;
    await runWithConcurrency(toUpload, DEFAULT_PART_CONCURRENCY, async ({ chunk, index }) => {
      const blockNum = index + 1;
      const form = new FormData();
      form.append('block', String(blockNum));
      form.append('file', bufferToBlob(chunk), `${filename}.part${blockNum}`);

      const partBody = await ous.request<CmdEnvelope<null>>({
        method: 'POST',
        path: '/ous/api/v2/block/upload/part',
        rawBody: form,
        signal: options.signal,
        timeoutMs: options.partTimeoutMs ?? DEFAULT_UPLOAD_PART_TIMEOUT_MS,
      });
      assertCmdOk(partBody, `block upload part ${blockNum}`);
      uploadedBytes += chunk.length;
      options.onProgress?.(uploadedBytes, totalBytes);
    });
  }

  private async pollUploadStatus(ous: OusHttpClient, options: UploadOptions): Promise<UploadResult> {
    const statusData = await pollUntil({
      fn: async () => {
        const body = await ous.request<CmdEnvelope<OusUploadStatusData>>({
          method: 'GET',
          path: '/ous/api/v2/upload/status',
          signal: options.signal,
        });
        return assertCmdSuccess(body, 'upload status');
      },
      isDone: (d) => d.status === OUS_STATUS_SUCCESS && typeof d.url === 'string' && d.url.length > 0,
      isFailed: (d) =>
        d.status !== undefined && (OUS_TERMINAL_FAILURE_STATUSES as readonly number[]).includes(d.status),
      failMessage: (d) =>
        `Upload failed (status=${d.status ?? 'unknown'}, errorCode=${d.errorCode ?? 'n/a'}, errorMsg=${d.errorMsg ?? 'n/a'})`,
      poll: {
        intervalMs: options.poll?.intervalMs ?? DEFAULT_POLL_INTERVAL_MS,
        timeoutMs: options.poll?.timeoutMs ?? DEFAULT_POLL_TIMEOUT_MS,
        signal: options.signal,
      },
    });

    return {
      url: statusData.url!,
      md5: statusData.md5 ?? '',
      uploadKey: statusData.uploadKey,
      obsTaskId: statusData.obsTaskId,
    };
  }
}

export function createAssetClient(config: AholoClientConfig = {}): AssetClient {
  return new AssetClient(config);
}
