export type { AholoClientConfig, AholoRegion } from './config.js';
export {
  DEFAULT_BASE_URL_CN,
  DEFAULT_BASE_URL_GLOBAL,
  DEFAULT_TIMEOUT_MS,
  resolveApiKey,
  resolveBaseUrl,
} from './config.js';

export {
  AholoError,
  AuthenticationError,
  BusinessError,
  PollingFailedError,
  PollingTimeoutError,
  RateLimitError,
  assertCmdOk,
  assertCmdSuccess,
  isApiErrorBody,
  isCmdEnvelope,
  throwForGatewayResponse,
  throwForHttpStatus,
  type ApiErrorBody,
  type CmdEnvelope,
} from './errors.js';

export { pollUntil, type PollOptions } from './poll.js';

export {
  AholoGatewayClient,
  BaseHttpClient,
  createGatewayClient,
  type HttpRequestOptions,
} from './gateway-client.js';

export { OusHttpClient, OUS_TOKEN_HEADER, type OusClientOptions } from './ous-client.js';
