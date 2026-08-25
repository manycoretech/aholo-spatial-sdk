import {
  assertCmdSuccess,
  type AholoClientConfig,
  type AholoGatewayClient,
  type CmdEnvelope,
} from '@manycore/aholo-sdk-core';

import { lux3dPath } from '../paths.js';
import type { ImageToFourViewRequest, Lux3dRequestOptions, TaskCreateResponse } from '../types.js';

export class ImageToFourViewResource {
  constructor(
    private readonly gateway: AholoGatewayClient,
    private readonly region: AholoClientConfig['region'],
  ) {}

  /** `POST /generate/image-to-four-view/task/create` */
  async create(body: ImageToFourViewRequest, options?: Lux3dRequestOptions): Promise<number> {
    const response = await this.gateway.gatewayRequest<TaskCreateResponse>({
      method: 'POST',
      path: lux3dPath(this.region, '/generate/image-to-four-view/task/create'),
      body,
      signal: options?.signal,
    });
    return assertCmdSuccess(response as CmdEnvelope<number>, 'imageToFourView.create');
  }
}
