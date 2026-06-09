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
    request: Omit<ImgTo3dRequest, 'img'> = {},
    options?: Lux3dRequestOptions,
  ): Promise<number> {
    const img = await fileToDataUrl(filePath);
    return this.create({ ...request, img }, options);
  }
}
