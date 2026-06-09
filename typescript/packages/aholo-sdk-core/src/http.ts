export type HttpMethod = 'GET' | 'POST' | 'PUT' | 'PATCH' | 'DELETE';

export interface HttpRequestOptions {
  method?: HttpMethod;
  path: string;
  query?: Record<string, string | number | boolean | undefined | null>;
  headers?: Record<string, string | undefined>;
  body?: unknown;
  /** When set, skip JSON serialization and do not force Content-Type. */
  rawBody?: BodyInit;
  timeoutMs?: number;
  signal?: AbortSignal;
}

export interface BaseHttpClientOptions {
  baseUrl: string;
  timeoutMs?: number;
  headers?: Record<string, string>;
  userAgent?: string;
  fetchImpl?: typeof fetch;
}

function buildUrl(baseUrl: string, path: string, query?: HttpRequestOptions['query']): string {
  const normalizedBase = baseUrl.replace(/\/$/, '');
  const normalizedPath = path.startsWith('/') ? path : `/${path}`;
  const url = new URL(`${normalizedBase}${normalizedPath}`);
  if (query) {
    for (const [key, value] of Object.entries(query)) {
      if (value === undefined || value === null) continue;
      url.searchParams.set(key, String(value));
    }
  }
  return url.toString();
}

async function readResponseBody(response: Response): Promise<unknown> {
  const contentType = response.headers.get('content-type') ?? '';
  if (contentType.includes('application/json')) {
    return response.json();
  }
  const text = await response.text();
  if (!text) return undefined;
  try {
    return JSON.parse(text);
  } catch {
    return text;
  }
}

/**
 * Low-level HTTP transport shared by gateway and OUS clients.
 */
export class BaseHttpClient {
  protected readonly baseUrl: string;
  protected readonly timeoutMs: number;
  protected readonly defaultHeaders: Record<string, string>;
  protected readonly fetchImpl: typeof fetch;

  constructor(options: BaseHttpClientOptions) {
    this.baseUrl = options.baseUrl.replace(/\/$/, '');
    this.timeoutMs = options.timeoutMs ?? 60_000;
    this.defaultHeaders = { ...(options.headers ?? {}) };
    this.fetchImpl = options.fetchImpl ?? fetch;
    if (options.userAgent) {
      this.defaultHeaders['User-Agent'] = options.userAgent;
    }
  }

  async request<T = unknown>(options: HttpRequestOptions): Promise<T> {
    const timeoutMs = options.timeoutMs ?? this.timeoutMs;
    const controller = new AbortController();
    const timeout = setTimeout(() => controller.abort(), timeoutMs);

    if (options.signal) {
      options.signal.addEventListener('abort', () => controller.abort(), { once: true });
    }

    const headers: Record<string, string> = { ...this.defaultHeaders };
    for (const [key, value] of Object.entries(options.headers ?? {})) {
      if (value !== undefined) headers[key] = value;
    }

    let body: BodyInit | undefined;
    if (options.rawBody !== undefined) {
      body = options.rawBody;
    } else if (options.body !== undefined) {
      body = JSON.stringify(options.body);
      if (!headers['Content-Type'] && !headers['content-type']) {
        headers['Content-Type'] = 'application/json';
      }
    }

    const url = buildUrl(this.baseUrl, options.path, options.query);

    try {
      const response = await this.fetchImpl(url, {
        method: options.method ?? 'GET',
        headers,
        body,
        signal: controller.signal,
      });

      const parsed = await readResponseBody(response);
      if (!response.ok) {
        const { throwForHttpStatus } = await import('./errors.js');
        throwForHttpStatus(response.status, parsed, `${options.method ?? 'GET'} ${options.path}`);
      }
      return parsed as T;
    } catch (error) {
      if (error instanceof Error && error.name === 'AbortError') {
        throw new Error(`Request timed out after ${timeoutMs}ms: ${options.method ?? 'GET'} ${options.path}`);
      }
      throw error;
    } finally {
      clearTimeout(timeout);
    }
  }
}
