import type { AholoClientConfig, AholoGatewayClient } from '@manycore/aholo-sdk-core';

import { worldPath } from '../paths.js';
import type { CreateWorldRequest, WorldAsyncOperation, WorldRequestOptions } from '../types.js';

export class ReconstructionsResource {
  constructor(
    private readonly gateway: AholoGatewayClient,
    private readonly region: AholoClientConfig['region'],
  ) {}

  /** `POST /world/v1/reconstructions` — Create a 3DGS reconstruction job. */
  create(body: CreateWorldRequest, options?: WorldRequestOptions): Promise<WorldAsyncOperation> {
    return this.gateway.gatewayRequest<WorldAsyncOperation>({
      method: 'POST',
      path: worldPath(this.region, '/reconstructions'),
      body,
      headers: options?.xSource ? { 'x-source': options.xSource } : undefined,
      signal: options?.signal,
    });
  }
}
