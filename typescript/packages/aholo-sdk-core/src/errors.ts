export interface ApiErrorBody {
  code?: number;
  message?: string;
  status?: string;
  details?: {
    reason?: string;
    message?: string;
    domain?: string;
    metaData?: Record<string, string>;
  };
}

export interface CmdEnvelope<T = unknown> {
  c: string;
  m?: string | null;
  d?: T | null;
}

export class AholoError extends Error {
  readonly statusCode?: number;
  readonly status?: string;
  readonly bizCode?: string;
  readonly body?: unknown;

  constructor(message: string, init?: { statusCode?: number; status?: string; bizCode?: string; body?: unknown }) {
    super(message);
    this.name = 'AholoError';
    this.statusCode = init?.statusCode;
    this.status = init?.status;
    this.bizCode = init?.bizCode;
    this.body = init?.body;
  }
}

export class AuthenticationError extends AholoError {
  constructor(message = 'Authentication failed', init?: ConstructorParameters<typeof AholoError>[1]) {
    super(message, init);
    this.name = 'AuthenticationError';
  }
}

export class RateLimitError extends AholoError {
  constructor(message = 'Rate limit exceeded', init?: ConstructorParameters<typeof AholoError>[1]) {
    super(message, init);
    this.name = 'RateLimitError';
  }
}

export class BusinessError extends AholoError {
  readonly cmdCode?: string;

  constructor(message: string, init?: ConstructorParameters<typeof AholoError>[1] & { cmdCode?: string }) {
    super(message, init);
    this.name = 'BusinessError';
    this.cmdCode = init?.cmdCode;
  }
}

export function isApiErrorBody(body: unknown): body is ApiErrorBody {
  return typeof body === 'object' && body !== null && 'status' in body && 'message' in body;
}

export function isCmdEnvelope(body: unknown): body is CmdEnvelope {
  return typeof body === 'object' && body !== null && 'c' in body;
}

export function throwForGatewayResponse(statusCode: number, body: unknown): never {
  if (isApiErrorBody(body)) {
    const bizCode = body.details?.metaData?.bizCode;
    const message = body.message ?? 'Gateway request failed';
    const init = {
      statusCode,
      status: body.status,
      bizCode,
      body,
    };
    if (statusCode === 401 || body.status === 'UNAUTHENTICATED') {
      throw new AuthenticationError(message, init);
    }
    if (statusCode === 429 || body.status === 'RESOURCE_EXHAUSTED') {
      throw new RateLimitError(message, init);
    }
    throw new AholoError(message, init);
  }

  throw new AholoError(`HTTP ${statusCode}`, { statusCode, body });
}

export function assertCmdOk(body: CmdEnvelope<unknown>, context: string): void {
  if (body.c !== '0') {
    throw new BusinessError(body.m || `${context} failed (c=${body.c})`, {
      cmdCode: body.c,
      body,
    });
  }
}

export function assertCmdSuccess<T>(body: CmdEnvelope<T>, context: string): T {
  assertCmdOk(body, context);
  if (body.d === undefined || body.d === null) {
    throw new BusinessError(`${context} succeeded but response data is empty`, { cmdCode: body.c, body });
  }
  return body.d;
}

export class PollingTimeoutError extends AholoError {
  readonly timeoutMs: number;

  constructor(message: string, init: { timeoutMs: number } & ConstructorParameters<typeof AholoError>[1]) {
    super(message, init);
    this.name = 'PollingTimeoutError';
    this.timeoutMs = init.timeoutMs;
  }
}

export class PollingFailedError extends AholoError {
  constructor(message: string, init?: ConstructorParameters<typeof AholoError>[1]) {
    super(message, init);
    this.name = 'PollingFailedError';
  }
}

export function throwForHttpStatus(statusCode: number, body: unknown, context: string): never {
  if (isCmdEnvelope(body)) {
    assertCmdSuccess(body, context);
  }
  throwForGatewayResponse(statusCode, body);
}
