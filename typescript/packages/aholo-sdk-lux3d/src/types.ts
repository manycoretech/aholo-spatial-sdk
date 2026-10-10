// API model types — sourced from the OpenAPI spec via openapi-typescript.
// Run `npm run generate` to refresh after spec changes.
import type { HumanoidAnimationId } from './humanoid-animations.js';
import type { components } from './generated/lux3d-api.js';

// --- Request types (direct from spec) ---
type GeneratedImgTo3dRequest       = components['schemas']['ImgTo3dRequest'];
type ImgTo3dRequestOptions         = Omit<GeneratedImgTo3dRequest, 'img' | 'imgs'>;
export type ImgTo3dRequest         = ImgTo3dRequestOptions &
  ({ img: string; imgs?: never } | { img?: never; imgs: string[] });
// Generated anyOf schemas collapse to `| unknown`; keep img|prompt as a real union.
export type MultimodalToImageRequest =
  | { img: string; prompt?: string }
  | { img?: string; prompt: string };
export type ImageToFourViewRequest =
  | { img: string; prompt?: string }
  | { img?: string; prompt: string };
export type TextTo3dRequest         = components['schemas']['TextTo3dRequest'];
export type MaterialTransferRequest = components['schemas']['MaterialTransferRequest'];
export type MultiFormatExportRequest = components['schemas']['MultiFormatExportRequest'];
export interface PartSplitRequest {
  glbUrl: string;
}
export interface ArticulationAnimationRequest {
  glbUrl: string;
  prompt: string;
}
export interface HumanoidAutoRigRequest {
  modelUrl: string;
}
export type HumanoidAnimationOutputMode = 'separate' | 'combined';
export type HumanoidAnimationOutFormat = 'glb' | 'fbx';
export interface HumanoidAnimationRetargetRequest {
  rigModelUrl: string;
  animationIds: HumanoidAnimationId[];
  animationOutputMode?: HumanoidAnimationOutputMode;
  outFormat?: HumanoidAnimationOutFormat;
  animateInPlace?: boolean;
}

// --- Response types (direct from spec) ---
export type TaskCreateResponse = components['schemas']['TaskCreateResponse'];
export type TaskOutput         = components['schemas']['TaskOutput'];
export type TaskQueryData      = components['schemas']['TaskQueryData'];
export type TaskQueryResponse  = components['schemas']['TaskQueryResponse'];
export type TaskListItem       = components['schemas']['TaskListItem'];
export type TaskListData       = components['schemas']['TaskListData'];
export type TaskListResponse   = components['schemas']['TaskListResponse'];

// --- Enum aliases (extracted from spec union literals) ---
export type Lux3dVersion      = NonNullable<ImgTo3dRequest['version']>;
export type Lux3dStyle        = NonNullable<TextTo3dRequest['style']>;
export type Lux3dOutputFormat = NonNullable<ImgTo3dRequest['outputFormat']>[number];
export type MaterialTransferOutputFormat = NonNullable<MaterialTransferRequest['outputFormat']>[number];
export type MultiFormatExportOutputFormat = NonNullable<MultiFormatExportRequest['outputFormat']>[number];
/** 0 init, 1 running, 3 success, 4 failed, 6 canceled */
export type Lux3dTaskStatus   = NonNullable<TaskQueryData['status']>;
/** List-filter statuses. Result items may still have status 6 (canceled). */
export type Lux3dTaskListStatus = 0 | 1 | 3 | 4;

export const LUX3D_STATUS_SUCCESS = 3 as const;
export const LUX3D_STATUS_FAILED  = 4 as const;
export const LUX3D_STATUS_CANCELED = 6 as const;

// --- SDK-facing result type (flattened from TaskQueryData, always present after polling) ---
export interface Lux3dTaskResult {
  taskId: number;
  bizId?: string;
  status: Lux3dTaskStatus;
  outputs: TaskOutput[];
}

/** Filters for `tasks.list()`. Omitted values are not sent as empty query params. */
export interface TaskListParams {
  page?: number;
  pageSize?: number;
  status?: Lux3dTaskListStatus;
  startTime?: number;
  endTime?: number;
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
