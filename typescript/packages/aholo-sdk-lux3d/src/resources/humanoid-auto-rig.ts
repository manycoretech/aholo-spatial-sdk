import {
  assertCmdSuccess,
  type AholoClientConfig,
  type AholoGatewayClient,
  type CmdEnvelope,
} from '@manycore/aholo-sdk-core';

import { lux3dPath } from '../paths.js';
import type { HumanoidAutoRigRequest, Lux3dRequestOptions, TaskCreateResponse } from '../types.js';

export class HumanoidAutoRigResource {
  constructor(
    private readonly gateway: AholoGatewayClient,
    private readonly region: AholoClientConfig['region'],
  ) {}

  /** `POST /animations/rig/task/create` */
  async create(body: HumanoidAutoRigRequest, options?: Lux3dRequestOptions): Promise<number> {
    if (!body.modelUrl?.trim()) {
      throw new TypeError('modelUrl is required');
    }
    const response = await this.gateway.gatewayRequest<TaskCreateResponse>({
      method: 'POST',
      path: lux3dPath(this.region, '/animations/rig/task/create'),
      body,
      signal: options?.signal,
    });
    return assertCmdSuccess(response as CmdEnvelope<number>, 'humanoidAutoRig.create');
  }
}
