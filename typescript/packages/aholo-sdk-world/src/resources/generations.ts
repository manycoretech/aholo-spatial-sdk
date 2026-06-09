import type { AholoClientConfig, AholoGatewayClient } from '@manycore/aholo-sdk-core';

import { worldPath } from '../paths.js';
import type { GenerateWorldRequest, WorldAsyncOperation, WorldRequestOptions } from '../types.js';

export class GenerationsResource {
  constructor(
    private readonly gateway: AholoGatewayClient,
    private readonly region: AholoClientConfig['region'],
  ) {}

  /** `POST /world/v1/generations` — Create a 3DGS generation job. */
  create(body: GenerateWorldRequest, options?: WorldRequestOptions): Promise<WorldAsyncOperation> {
    return this.gateway.gatewayRequest<WorldAsyncOperation>({
      method: 'POST',
      path: worldPath(this.region, '/generations'),
      body,
      headers: options?.xSource ? { 'x-source': options.xSource } : undefined,
      signal: options?.signal,
    });
  }
}
