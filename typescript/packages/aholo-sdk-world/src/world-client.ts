import {
  createGatewayClient,
  pollUntil,
  type AholoClientConfig,
  type AholoGatewayClient,
} from '@manycore/aholo-sdk-core';

import { worldPath } from './paths.js';
import { GenerationsResource } from './resources/generations.js';
import { ReconstructionsResource } from './resources/reconstructions.js';
import {
  WORLD_TERMINAL_FAILURE_STATUSES,
  type WaitForWorldOptions,
  type WorldDetail,
  type WorldListQueryRequest,
  type WorldPagedList,
  type WorldRequestOptions,
} from './types.js';

const DEFAULT_POLL_INTERVAL_MS = 5_000;
const DEFAULT_POLL_TIMEOUT_MS = 86_400_000; // 24 hours

function requestHeaders(options?: WorldRequestOptions): Record<string, string | undefined> | undefined {
  if (!options?.xSource) return undefined;
  return { 'x-source': options.xSource };
}

/**
 * Aholo 3DGS world API client.
 *
 * Stainless-style resource access:
 * ```ts
 * const world = createWorldClient({ apiKey: '...' })
 * await world.reconstructions.create({ ... })
 * await world.generations.create({ ... })
 * await world.retrieve(worldId)
 * await world.list()
 * await world.waitFor(worldId)
 * ```
 */
export class WorldClient {
  /** POST /world/v1/reconstructions */
  readonly reconstructions: ReconstructionsResource;
  /** POST /world/v1/generations */
  readonly generations: GenerationsResource;

  private readonly gateway: AholoGatewayClient;
  private readonly config: AholoClientConfig;

  constructor(config: AholoClientConfig = {}) {
    this.config = config;
    this.gateway = createGatewayClient(config);
    this.reconstructions = new ReconstructionsResource(this.gateway, config.region);
    this.generations = new GenerationsResource(this.gateway, config.region);
  }

  /** `GET /world/v1/{worldId}` */
  retrieve(worldId: string, options?: WorldRequestOptions): Promise<WorldDetail> {
    return this.gateway.gatewayRequest<WorldDetail>({
      method: 'GET',
      path: worldPath(this.config.region, `/${encodeURIComponent(worldId)}`),
      headers: requestHeaders(options),
      signal: options?.signal,
    });
  }

  /** `POST /world/v1/list` */
  list(body: WorldListQueryRequest = {}, options?: WorldRequestOptions): Promise<WorldPagedList> {
    return this.gateway.gatewayRequest<WorldPagedList>({
      method: 'POST',
      path: worldPath(this.config.region, '/list'),
      body,
      headers: requestHeaders(options),
      signal: options?.signal,
    });
  }

  /** Poll world detail until SUCCEEDED or a terminal failure status. */
  waitFor(worldId: string, options: WaitForWorldOptions = {}): Promise<WorldDetail> {
    return pollUntil({
      fn: () => this.retrieve(worldId, { signal: options.signal }),
      isDone: (detail) => detail.status === 'SUCCEEDED',
      isFailed: (detail) =>
        detail.status !== undefined &&
        (WORLD_TERMINAL_FAILURE_STATUSES as readonly string[]).includes(detail.status),
      failMessage: (detail) => `World task failed (worldId=${worldId}, status=${detail.status ?? 'unknown'})`,
      poll: {
        intervalMs: options.intervalMs ?? DEFAULT_POLL_INTERVAL_MS,
        timeoutMs: options.timeoutMs ?? DEFAULT_POLL_TIMEOUT_MS,
        signal: options.signal,
      },
    });
  }

}

export function createWorldClient(config: AholoClientConfig = {}): WorldClient {
  return new WorldClient(config);
}
