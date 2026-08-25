import {
  assertCmdSuccess,
  type AholoClientConfig,
  type AholoGatewayClient,
  type CmdEnvelope,
} from '@manycore/aholo-sdk-core';

import { fileToDataUrl } from '../image.js';
import { lux3dPath } from '../paths.js';
import type { ImgTo3dRequest, Lux3dRequestOptions, TaskCreateResponse } from '../types.js';

function assertTaskId(body: TaskCreateResponse, context: string): number {
  return assertCmdSuccess(body as CmdEnvelope<number>, context);
}

export class ImgTo3dResource {
  constructor(
    private readonly gateway: AholoGatewayClient,
    private readonly region: AholoClientConfig['region'],
  ) {}

  /** `POST /generate/img-to-3d/task/create` */
  async create(body: ImgTo3dRequest, options?: Lux3dRequestOptions): Promise<number> {
    const hasImg = body.img !== undefined;
    const hasImgs = body.imgs !== undefined;
    if (hasImg === hasImgs) {
      throw new TypeError('Provide exactly one of img or imgs');
    }
    if (body.imgs && (body.imgs.length < 1 || body.imgs.length > 32)) {
      throw new RangeError('imgs must contain between 1 and 32 images');
    }
    const response = await this.gateway.gatewayRequest<TaskCreateResponse>({
      method: 'POST',
      path: lux3dPath(this.region, '/generate/img-to-3d/task/create'),
      body,
      signal: options?.signal,
    });
    return assertTaskId(response, 'imgTo3d.create');
  }

  /** Create img-to-3D task from a local image file (encodes to Data URL automatically). */
  async createFromFile(
    filePath: string,
    request: Omit<ImgTo3dRequest, 'img' | 'imgs'>,
    options?: Lux3dRequestOptions,
  ): Promise<number> {
    const img = await fileToDataUrl(filePath);
    return this.create({ ...request, img }, options);
  }

  /**
   * Create a G1 multi-view img-to-3D task from local image files
   * (encodes each file to a Data URL and sends as `imgs`).
   */
  async createFromFiles(
    filePaths: string[],
    request: Omit<ImgTo3dRequest, 'img' | 'imgs'>,
    options?: Lux3dRequestOptions,
  ): Promise<number> {
    const imgs = await Promise.all(filePaths.map((path) => fileToDataUrl(path)));
    return this.create({ ...request, imgs }, options);
  }
}
