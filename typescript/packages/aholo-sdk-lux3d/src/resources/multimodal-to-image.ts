import {
  assertCmdSuccess,
  type AholoClientConfig,
  type AholoGatewayClient,
  type CmdEnvelope,
} from '@manycore/aholo-sdk-core';

import { fileToDataUrl } from '../image.js';
import { requireImgOrPrompt } from '../img-or-prompt.js';
import { lux3dPath } from '../paths.js';
import type { Lux3dRequestOptions, MultimodalToImageRequest, TaskCreateResponse } from '../types.js';

export class MultimodalToImageResource {
  constructor(
    private readonly gateway: AholoGatewayClient,
    private readonly region: AholoClientConfig['region'],
  ) {}

  /** `POST /generate/multimodal-to-image/task/create` */
  async create(body: MultimodalToImageRequest, options?: Lux3dRequestOptions): Promise<number> {
    requireImgOrPrompt(body, 'multimodalToImage.create');
    const response = await this.gateway.gatewayRequest<TaskCreateResponse>({
      method: 'POST',
      path: lux3dPath(this.region, '/generate/multimodal-to-image/task/create'),
      body,
      signal: options?.signal,
    });
    return assertCmdSuccess(response as CmdEnvelope<number>, 'multimodalToImage.create');
  }

  /**
   * Create a multimodal-to-image task from a local image file
   * (encodes to a Data URL and sends as `img`). Prompt remains optional.
   */
  async createFromFile(
    filePath: string,
    request: { prompt?: string } = {},
    options?: Lux3dRequestOptions,
  ): Promise<number> {
    const hasPrompt = typeof request.prompt === 'string' && request.prompt.trim() !== '';
    if ((!filePath || filePath.trim() === '') && !hasPrompt) {
      throw new TypeError('multimodalToImage.createFromFile: provide at least one of file or prompt');
    }
    if (!filePath || filePath.trim() === '') {
      return this.create({ prompt: request.prompt as string }, options);
    }
    const img = await fileToDataUrl(filePath);
    return this.create({ ...request, img }, options);
  }
}
