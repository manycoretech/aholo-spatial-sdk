import {
  assertCmdSuccess,
  type AholoClientConfig,
  type AholoGatewayClient,
  type CmdEnvelope,
} from '@manycore/aholo-sdk-core';

import { lux3dPath } from '../paths.js';
import type { Lux3dRequestOptions, TaskCreateResponse, TextTo3dRequest } from '../types.js';

function assertTaskId(body: TaskCreateResponse, context: string): number {
  return assertCmdSuccess(body as CmdEnvelope<number>, context);
}

export class TextTo3dResource {
  constructor(
    private readonly gateway: AholoGatewayClient,
    private readonly region: AholoClientConfig['region'],
  ) {}

  /** `POST /generate/text-to-3d/task/create` */
  async create(body: TextTo3dRequest, options?: Lux3dRequestOptions): Promise<number> {
    const response = await this.gateway.gatewayRequest<TaskCreateResponse>({
      method: 'POST',
      path: lux3dPath(this.region, '/generate/text-to-3d/task/create'),
      body,
      signal: options?.signal,
    });
    return assertTaskId(response, 'textTo3d.create');
  }
}
