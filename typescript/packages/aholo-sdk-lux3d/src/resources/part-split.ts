import {
  assertCmdSuccess,
  type AholoClientConfig,
  type AholoGatewayClient,
  type CmdEnvelope,
} from '@manycore/aholo-sdk-core';

import { lux3dPath } from '../paths.js';
import type { Lux3dRequestOptions, PartSplitRequest, TaskCreateResponse } from '../types.js';

export class PartSplitResource {
  constructor(
    private readonly gateway: AholoGatewayClient,
    private readonly region: AholoClientConfig['region'],
  ) {}

  /** `POST /part-split/task/create` */
  async create(body: PartSplitRequest, options?: Lux3dRequestOptions): Promise<number> {
    if (!body.glbUrl?.trim()) {
      throw new TypeError('glbUrl is required');
    }
    const response = await this.gateway.gatewayRequest<TaskCreateResponse>({
      method: 'POST',
      path: lux3dPath(this.region, '/part-split/task/create'),
      body,
      signal: options?.signal,
    });
    return assertCmdSuccess(response as CmdEnvelope<number>, 'partSplit.create');
  }
}
