import {
  assertCmdSuccess,
  type AholoClientConfig,
  type AholoGatewayClient,
  type CmdEnvelope,
} from '@manycore/aholo-sdk-core';

import { lux3dPath } from '../paths.js';
import type { Lux3dRequestOptions, MaterialTransferRequest, TaskCreateResponse } from '../types.js';

function assertTaskId(body: TaskCreateResponse, context: string): number {
  return assertCmdSuccess(body as CmdEnvelope<number>, context);
}

export class MaterialTransferResource {
  constructor(
    private readonly gateway: AholoGatewayClient,
    private readonly region: AholoClientConfig['region'],
  ) {}

  /** `POST /generate/material-transfer/task/create` */
  async create(body: MaterialTransferRequest, options?: Lux3dRequestOptions): Promise<number> {
    const response = await this.gateway.gatewayRequest<TaskCreateResponse>({
      method: 'POST',
      path: lux3dPath(this.region, '/generate/material-transfer/task/create'),
      body,
      signal: options?.signal,
    });
    return assertTaskId(response, 'materialTransfer.create');
  }
}
