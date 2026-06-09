/** OUS upload task status from `GET /ous/api/v2/upload/status`. */
export type OusUploadTaskStatus = 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8;

export const OUS_STATUS_SUCCESS = 5 as const;
// TODO: verify with OUS API docs whether status 7 is also a terminal failure.
export const OUS_TERMINAL_FAILURE_STATUSES: readonly OusUploadTaskStatus[] = [6, 8];

export interface UploadToken {
  ousToken: string;
  globalDomain: string;
  blockSize: number;
}

export interface UploadResult {
  url: string;
  md5: string;
  uploadKey?: string;
  obsTaskId?: string;
}

export interface UploadOptions {
  /** Original filename; defaults to `upload.bin` or the basename of a local path. */
  filename?: string;
  metadata?: string;
  customPrefix?: string;
  customFilename?: string;
  /** Per-block upload timeout in ms. Defaults to 2 minutes. */
  partTimeoutMs?: number;
  /** Called after each block (or after single upload). `uploaded` and `total` are in bytes. */
  onProgress?: (uploaded: number, total: number) => void;
  /** Poll interval / timeout overrides for status polling. */
  poll?: {
    intervalMs?: number;
    timeoutMs?: number;
  };
  signal?: AbortSignal;
}

export interface OusBlockInitData {
  taskId: number;
  lackBlocks?: string[] | null;
  progress?: number | null;
  deduplicated?: boolean | null;
}

export interface OusUploadStatusData {
  status?: OusUploadTaskStatus;
  uploadKey?: string;
  url?: string;
  md5?: string;
  obsTaskId?: string;
  errorCode?: number | null;
  errorMsg?: string | null;
}
