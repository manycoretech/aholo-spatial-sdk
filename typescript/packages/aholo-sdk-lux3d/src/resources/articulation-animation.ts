import {
  assertCmdSuccess,
  type AholoClientConfig,
  type AholoGatewayClient,
  type CmdEnvelope,
} from '@manycore/aholo-sdk-core';

import { lux3dPath } from '../paths.js';
import type {
  ArticulationAnimationRequest,
  Lux3dRequestOptions,
  TaskCreateResponse,
} from '../types.js';

export class ArticulationAnimationResource {
  constructor(
    private readonly gateway: AholoGatewayClient,
    private readonly region: AholoClientConfig['region'],
  ) {}

  /** `POST /articulation-animation/task/create` */
  async create(body: ArticulationAnimationRequest, options?: Lux3dRequestOptions): Promise<number> {
    if (!body.glbUrl?.trim() || !body.prompt?.trim()) {
      throw new TypeError('glbUrl and prompt are required');
    }
    const response = await this.gateway.gatewayRequest<TaskCreateResponse>({
      method: 'POST',
      path: lux3dPath(this.region, '/articulation-animation/task/create'),
      body: { glbUrl: body.glbUrl, prompt: body.prompt },
      signal: options?.signal,
    });
    return assertCmdSuccess(response as CmdEnvelope<number>, 'articulationAnimation.create');
  }
}
