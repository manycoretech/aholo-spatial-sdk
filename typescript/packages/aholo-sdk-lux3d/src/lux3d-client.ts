import { createGatewayClient, type AholoClientConfig, type AholoGatewayClient } from '@manycore/aholo-sdk-core';

import { ArticulationAnimationResource } from './resources/articulation-animation.js';
import { HumanoidAnimationRetargetResource } from './resources/humanoid-animation-retarget.js';
import { HumanoidAutoRigResource } from './resources/humanoid-auto-rig.js';
import { ImageToFourViewResource } from './resources/image-to-four-view.js';
import { ImgTo3dResource } from './resources/img-to-3d.js';
import { MaterialTransferResource } from './resources/material-transfer.js';
import { MultimodalToImageResource } from './resources/multimodal-to-image.js';
import { MultiFormatExportResource } from './resources/multi-format-export.js';
import { PartSplitResource } from './resources/part-split.js';
import { TasksResource } from './resources/tasks.js';
import { TextTo3dResource } from './resources/text-to-3d.js';

/**
 * Aholo Lux3D API client.
 *
 * Stainless-style resource access:
 * ```ts
 * const lux3d = createLux3dClient({ apiKey: '...' })
 * await lux3d.multimodalToImage.create({ prompt: '...' })
 * await lux3d.imageToFourView.create({ img: '...' })
 * await lux3d.imageToFourView.create({ prompt: '...' })
 * await lux3d.imgTo3d.create({ img: '...', version: 'G1' })
 * await lux3d.imgTo3d.createFromFile('/path/to/image.png', { version: 'G1' })
 * await lux3d.textTo3d.create({ prompt: '...', version: 'G1' })
 * await lux3d.materialTransfer.create({ img: '...', meshUrl: '...', version: 'v3.0-standard' })
 * await lux3d.multiFormatExport.create({ modelUrl: '...', outputFormat: ['usdz'] })
 * await lux3d.partSplit.create({ glbUrl: '...' })
 * await lux3d.articulationAnimation.create({ glbUrl: '...', prompt: '...' })
 * await lux3d.humanoidAutoRig.create({ modelUrl: '...' })
 * await lux3d.humanoidAnimationRetarget.create({ rigModelUrl: '...', animationIds: ['Idle_Loop'] })
 * await lux3d.tasks.retrieve(taskId)
 * await lux3d.tasks.list()
 * await lux3d.tasks.waitFor(taskId)
 * ```
 */
export class Lux3dClient {
  readonly multimodalToImage: MultimodalToImageResource;
  readonly imageToFourView: ImageToFourViewResource;
  readonly imgTo3d: ImgTo3dResource;
  readonly textTo3d: TextTo3dResource;
  readonly materialTransfer: MaterialTransferResource;
  readonly multiFormatExport: MultiFormatExportResource;
  readonly partSplit: PartSplitResource;
  readonly articulationAnimation: ArticulationAnimationResource;
  readonly humanoidAutoRig: HumanoidAutoRigResource;
  readonly humanoidAnimationRetarget: HumanoidAnimationRetargetResource;
  readonly tasks: TasksResource;

  private readonly gateway: AholoGatewayClient;

  constructor(config: AholoClientConfig = {}) {
    this.gateway = createGatewayClient(config);
    this.multimodalToImage = new MultimodalToImageResource(this.gateway, config.region);
    this.imageToFourView = new ImageToFourViewResource(this.gateway, config.region);
    this.imgTo3d = new ImgTo3dResource(this.gateway, config.region);
    this.textTo3d = new TextTo3dResource(this.gateway, config.region);
    this.materialTransfer = new MaterialTransferResource(this.gateway, config.region);
    this.multiFormatExport = new MultiFormatExportResource(this.gateway, config.region);
    this.partSplit = new PartSplitResource(this.gateway, config.region);
    this.articulationAnimation = new ArticulationAnimationResource(this.gateway, config.region);
    this.humanoidAutoRig = new HumanoidAutoRigResource(this.gateway, config.region);
    this.humanoidAnimationRetarget = new HumanoidAnimationRetargetResource(this.gateway, config.region);
    this.tasks = new TasksResource(this.gateway, config.region);
  }

}

export function createLux3dClient(config: AholoClientConfig = {}): Lux3dClient {
  return new Lux3dClient(config);
}
