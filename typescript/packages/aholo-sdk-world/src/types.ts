// API model types — sourced from the OpenAPI spec via openapi-typescript.
// Run `npm run generate` to refresh after spec changes.
import type { components } from './generated/world-api.js';

// --- Request / response types (direct from spec) ---
export type CreateWorldRequest    = components['schemas']['CreateWorldRequest'];
export type GenerateWorldRequest  = components['schemas']['GenerateWorldRequest'];
export type WorldAsyncOperation   = components['schemas']['WorldAsyncOperation'];
export type WorldResourceItem     = components['schemas']['WorldResourceItem'];
export type WorldDetail           = components['schemas']['WorldDetail'];
export type WorldPagedList        = components['schemas']['WorldPagedList'];
export type WorldListQueryRequest = components['schemas']['WorldListQueryRequest'];
export type SplatFileUrls         = components['schemas']['SplatFileUrls'];
export type WorldSplatBundle        = components['schemas']['WorldSplatBundle'];
export type WorldImagery            = components['schemas']['WorldImagery'];
export type WorldSemanticsMetadata  = components['schemas']['WorldSemanticsMetadata'];
export type WorldAssetBundle        = components['schemas']['WorldAssetBundle'];

// --- Enum aliases (spec uses longer internal names) ---
export type WorldScene        = components['schemas']['WorldOpenApiProjectScene'];
export type WorldTaskQuality  = components['schemas']['WorldOpenApiTaskQuality'];
export type WorldResourceType = components['schemas']['WorldOpenApiResourceType'];
export type WorldTaskStatus   = components['schemas']['WorldOpenApiTaskStatus'];
export type WorldUpAxis       = components['schemas']['WorldOpenApiUpAxis'];

// --- SDK-specific options (not part of the HTTP API) ---
export interface WorldRequestOptions {
  /** Passed as the `x-source` request header. */
  xSource?: string;
  signal?: AbortSignal;
}

export interface WaitForWorldOptions {
  intervalMs?: number;
  timeoutMs?: number;
  signal?: AbortSignal;
}

export const WORLD_TERMINAL_FAILURE_STATUSES: readonly WorldTaskStatus[] = ['FAILED', 'CANCELED', 'TIMEOUT', 'REJECTED'];
