import { PollingFailedError, PollingTimeoutError } from './errors.js';

export interface PollOptions {
  /** Poll interval in ms. Default: 500. */
  intervalMs?: number;
  /** Max wait in ms. Default: 300000 (5 min). */
  timeoutMs?: number;
  signal?: AbortSignal;
}

function sleep(ms: number, signal?: AbortSignal): Promise<void> {
  return new Promise((resolve, reject) => {
    if (signal?.aborted) {
      reject(signal.reason ?? new Error('Polling aborted'));
      return;
    }
    const timer = setTimeout(resolve, ms);
    signal?.addEventListener(
      'abort',
      () => {
        clearTimeout(timer);
        reject(signal.reason ?? new Error('Polling aborted'));
      },
      { once: true },
    );
  });
}

export async function pollUntil<T>(options: {
  fn: () => Promise<T>;
  isDone: (result: T) => boolean;
  isFailed?: (result: T) => boolean;
  failMessage?: (result: T) => string;
  poll?: PollOptions;
}): Promise<T> {
  const intervalMs = options.poll?.intervalMs ?? 500;
  const timeoutMs = options.poll?.timeoutMs ?? 300_000;
  const started = Date.now();

  while (true) {
    const result = await options.fn();

    if (options.isFailed?.(result)) {
      throw new PollingFailedError(options.failMessage?.(result) ?? 'Polling failed');
    }
    if (options.isDone(result)) {
      return result;
    }

    if (Date.now() - started >= timeoutMs) {
      throw new PollingTimeoutError(`Polling timed out after ${timeoutMs}ms`, { timeoutMs });
    }

    await sleep(intervalMs, options.poll?.signal);
  }
}
