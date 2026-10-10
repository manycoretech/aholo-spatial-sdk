import {
  assertCmdSuccess,
  type AholoClientConfig,
  type AholoGatewayClient,
  type CmdEnvelope,
} from '@manycore/aholo-sdk-core';

import { HUMANOID_ANIMATION_ID_SET } from '../humanoid-animations.js';
import { lux3dPath } from '../paths.js';
import type {
  HumanoidAnimationRetargetRequest,
  Lux3dRequestOptions,
  TaskCreateResponse,
} from '../types.js';

const OUTPUT_MODES = new Set(['separate', 'combined']);
const OUT_FORMATS = new Set(['glb', 'fbx']);

export class HumanoidAnimationRetargetResource {
  constructor(
    private readonly gateway: AholoGatewayClient,
    private readonly region: AholoClientConfig['region'],
  ) {}

  /** `POST /animations/retarget/task/create` */
  async create(
    body: HumanoidAnimationRetargetRequest,
    options?: Lux3dRequestOptions,
  ): Promise<number> {
    if (!body.rigModelUrl?.trim()) {
      throw new TypeError('rigModelUrl is required');
    }
    const ids = body.animationIds;
    if (!ids || ids.length < 1 || ids.length > 10) {
      throw new RangeError('animationIds must contain 1 to 10 ids');
    }
    if (new Set(ids).size !== ids.length || ids.some((id) => !HUMANOID_ANIMATION_ID_SET.has(id))) {
      throw new TypeError('animationIds must be unique values from the humanoid animation list');
    }
    if (body.animationOutputMode !== undefined && !OUTPUT_MODES.has(body.animationOutputMode)) {
      throw new TypeError('animationOutputMode must be separate or combined');
    }
    if (body.outFormat !== undefined && !OUT_FORMATS.has(body.outFormat)) {
      throw new TypeError('outFormat must be glb or fbx');
    }
    const response = await this.gateway.gatewayRequest<TaskCreateResponse>({
      method: 'POST',
      path: lux3dPath(this.region, '/animations/retarget/task/create'),
      body,
      signal: options?.signal,
    });
    return assertCmdSuccess(response as CmdEnvelope<number>, 'humanoidAnimationRetarget.create');
  }
}
