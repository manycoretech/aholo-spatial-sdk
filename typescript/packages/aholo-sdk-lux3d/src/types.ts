// API model types — sourced from the OpenAPI spec via openapi-typescript.
// Run `npm run generate` to refresh after spec changes.
import type { components } from './generated/lux3d-api.js';

// --- Request types (direct from spec) ---
export type ImgTo3dRequest          = components['schemas']['ImgTo3dRequest'];
export type TextTo3dRequest         = components['schemas']['TextTo3dRequest'];
export type MaterialTransferRequest = components['schemas']['MaterialTransferRequest'];

// --- Response types (direct from spec) ---
export type TaskCreateResponse = components['schemas']['TaskCreateResponse'];
export type TaskOutput         = components['schemas']['TaskOutput'];
export type TaskQueryData      = components['schemas']['TaskQueryData'];
export type TaskQueryResponse  = components['schemas']['TaskQueryResponse'];

// --- Enum aliases (extracted from spec union literals) ---
export type Lux3dVersion    = NonNullable<ImgTo3dRequest['version']>;
export type Lux3dStyle      = NonNullable<TextTo3dRequest['style']>;
/** 0 init, 1 running, 3 success, 4 failed */
export type Lux3dTaskStatus = NonNullable<TaskQueryData['status']>;

export const LUX3D_STATUS_SUCCESS = 3 as const;
export const LUX3D_STATUS_FAILED  = 4 as const;

// --- SDK-facing result type (flattened from TaskQueryData, always present after polling) ---
export interface Lux3dTaskResult {
  taskId: number;
  status: Lux3dTaskStatus;
  outputs: TaskOutput[];
}

// --- SDK-specific options (not part of the HTTP API) ---
export interface WaitForLux3dTaskOptions {
  intervalMs?: number;
  timeoutMs?: number;
  signal?: AbortSignal;
}

export interface Lux3dRequestOptions {
  signal?: AbortSignal;
}
