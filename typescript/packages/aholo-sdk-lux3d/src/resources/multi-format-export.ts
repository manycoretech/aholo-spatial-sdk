import {
  assertCmdSuccess,
  type AholoClientConfig,
  type AholoGatewayClient,
  type CmdEnvelope,
} from '@manycore/aholo-sdk-core';

import { lux3dPath } from '../paths.js';
import type { Lux3dRequestOptions, MultiFormatExportRequest, TaskCreateResponse } from '../types.js';

export class MultiFormatExportResource {
  constructor(
    private readonly gateway: AholoGatewayClient,
    private readonly region: AholoClientConfig['region'],
  ) {}

  /** `POST /multi-format-export/task/create` */
  async create(body: MultiFormatExportRequest, options?: Lux3dRequestOptions): Promise<number> {
    const response = await this.gateway.gatewayRequest<TaskCreateResponse>({
      method: 'POST',
      path: lux3dPath(this.region, '/multi-format-export/task/create'),
      body,
      signal: options?.signal,
    });
    return assertCmdSuccess(response as CmdEnvelope<number>, 'multiFormatExport.create');
  }
}
