import { createGatewayClient, type AholoClientConfig, type AholoGatewayClient } from '@manycore/aholo-sdk-core';

import { ImgTo3dResource } from './resources/img-to-3d.js';
import { MaterialTransferResource } from './resources/material-transfer.js';
import { TasksResource } from './resources/tasks.js';
import { TextTo3dResource } from './resources/text-to-3d.js';

/**
 * Aholo Lux3D API client.
 *
 * Stainless-style resource access:
 * ```ts
 * const lux3d = createLux3dClient({ apiKey: '...' })
 * await lux3d.imgTo3d.create({ img: '...' })
 * await lux3d.imgTo3d.createFromFile('/path/to/image.png')
 * await lux3d.textTo3d.create({ prompt: '...' })
 * await lux3d.materialTransfer.create({ img: '...', meshUrl: '...' })
 * await lux3d.tasks.retrieve(taskId)
 * await lux3d.tasks.waitFor(taskId)
 * ```
 */
export class Lux3dClient {
  readonly imgTo3d: ImgTo3dResource;
  readonly textTo3d: TextTo3dResource;
  readonly materialTransfer: MaterialTransferResource;
  readonly tasks: TasksResource;

  private readonly gateway: AholoGatewayClient;

  constructor(config: AholoClientConfig = {}) {
    this.gateway = createGatewayClient(config);
    this.imgTo3d = new ImgTo3dResource(this.gateway, config.region);
    this.textTo3d = new TextTo3dResource(this.gateway, config.region);
    this.materialTransfer = new MaterialTransferResource(this.gateway, config.region);
    this.tasks = new TasksResource(this.gateway, config.region);
  }

}

export function createLux3dClient(config: AholoClientConfig = {}): Lux3dClient {
  return new Lux3dClient(config);
}
