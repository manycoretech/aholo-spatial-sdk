export type AholoRegion = 'cn' | 'com';

export interface AholoClientConfig {
  /** Platform API key; falls back to `AHOLO_API_KEY` when omitted. */
  apiKey?: string;
  /** Gateway base URL, e.g. https://api.aholo3d.cn */
  baseUrl?: string;
  /** Shorthand for preset gateway hosts when `baseUrl` is not set. */
  region?: AholoRegion;
  /** Per-request timeout in milliseconds. Default: 60000. */
  timeoutMs?: number;
  /** Optional User-Agent suffix. */
  userAgent?: string;
  /** Inject fetch (tests / custom runtimes). Defaults to global fetch. */
  fetch?: typeof fetch;
}

export const DEFAULT_BASE_URL_CN = 'https://api.aholo3d.cn';
export const DEFAULT_BASE_URL_GLOBAL = 'https://api.aholo3d.com';

export const DEFAULT_TIMEOUT_MS = 60_000;

export function resolveBaseUrl(config: Pick<AholoClientConfig, 'baseUrl' | 'region'>): string {
  if (config.baseUrl) {
    return config.baseUrl.replace(/\/$/, '');
  }
  if (config.region === 'com') {
    return DEFAULT_BASE_URL_GLOBAL;
  }
  return DEFAULT_BASE_URL_CN;
}

export function resolveApiKey(config: Pick<AholoClientConfig, 'apiKey'>): string {
  const key = config.apiKey ?? process.env.AHOLO_API_KEY;
  if (!key) {
    throw new Error('Missing API key: set `apiKey` in config or AHOLO_API_KEY env var.');
  }
  return key;
}
