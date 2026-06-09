import {
  assertCmdSuccess,
  BusinessError,
  pollUntil,
  type AholoClientConfig,
  type AholoGatewayClient,
  type CmdEnvelope,
} from '@manycore/aholo-sdk-core';

import { lux3dPath } from '../paths.js';
import {
  LUX3D_STATUS_FAILED,
  LUX3D_STATUS_SUCCESS,
  type Lux3dRequestOptions,
  type Lux3dTaskResult,
  type TaskQueryData,
  type TaskQueryResponse,
  type WaitForLux3dTaskOptions,
} from '../types.js';

const DEFAULT_POLL_INTERVAL_MS = 12_000;
const DEFAULT_POLL_TIMEOUT_MS = 600_000;

function assertQueryData(body: TaskQueryResponse, context: string): TaskQueryData {
  return assertCmdSuccess(body as CmdEnvelope<TaskQueryData>, context);
}

function toTaskResult(data: TaskQueryData): Lux3dTaskResult {
  if (data.taskId == null || data.status == null) {
    throw new BusinessError('Lux3D task query returned incomplete data', { body: data });
  }
  return {
    taskId: data.taskId,
    status: data.status,
    outputs: data.outputs ?? [],
  };
}

export class TasksResource {
  constructor(
    private readonly gateway: AholoGatewayClient,
    private readonly region: AholoClientConfig['region'],
  ) {}

  /** `GET /generate/task/get` */
  retrieve(taskId: number | string, options?: Lux3dRequestOptions): Promise<Lux3dTaskResult> {
    return this.gateway
      .gatewayRequest<TaskQueryResponse>({
        method: 'GET',
        path: lux3dPath(this.region, '/generate/task/get'),
        query: { taskid: String(taskId) },
        signal: options?.signal,
      })
      .then((body) => toTaskResult(assertQueryData(body, 'tasks.retrieve')));
  }

  /** Poll task until success or failure. */
  waitFor(taskId: number | string, options: WaitForLux3dTaskOptions = {}): Promise<Lux3dTaskResult> {
    return pollUntil({
      fn: () => this.retrieve(taskId, { signal: options.signal }),
      isDone: (result) => result.status === LUX3D_STATUS_SUCCESS,
      isFailed: (result) => result.status === LUX3D_STATUS_FAILED,
      failMessage: (result) => `Lux3D task failed (taskId=${taskId}, status=${result.status})`,
      poll: {
        intervalMs: options.intervalMs ?? DEFAULT_POLL_INTERVAL_MS,
        timeoutMs: options.timeoutMs ?? DEFAULT_POLL_TIMEOUT_MS,
        signal: options.signal,
      },
    });
  }
}
