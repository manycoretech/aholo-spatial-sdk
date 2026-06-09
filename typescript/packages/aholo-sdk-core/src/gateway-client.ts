import { resolveApiKey, resolveBaseUrl, DEFAULT_TIMEOUT_MS, type AholoClientConfig } from './config.js';
import { BaseHttpClient, type HttpRequestOptions } from './http.js';

const DEFAULT_USER_AGENT = 'aholo-sdk-core/1.1.0';

/**
 * HTTP client for Aholo Open Platform gateway (`Authorization: <apiKey>`).
 */
export class AholoGatewayClient extends BaseHttpClient {
  readonly apiKey: string;

  constructor(config: AholoClientConfig = {}) {
    const apiKey = resolveApiKey(config);
    super({
      baseUrl: resolveBaseUrl(config),
      timeoutMs: config.timeoutMs ?? DEFAULT_TIMEOUT_MS,
      headers: {
        Authorization: apiKey,
      },
      userAgent: config.userAgent ? `${DEFAULT_USER_AGENT} ${config.userAgent}` : DEFAULT_USER_AGENT,
      fetchImpl: config.fetch,
    });
    this.apiKey = apiKey;
  }

  gatewayRequest<T = unknown>(options: HttpRequestOptions): Promise<T> {
    return this.request<T>(options);
  }
}

export function createGatewayClient(config: AholoClientConfig = {}): AholoGatewayClient {
  return new AholoGatewayClient(config);
}

export { BaseHttpClient, type HttpRequestOptions } from './http.js';
