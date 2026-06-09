import { BaseHttpClient, type BaseHttpClientOptions } from './http.js';

const OUS_TOKEN_HEADER = 'ous-token-v2';

export interface OusClientOptions extends Omit<BaseHttpClientOptions, 'headers'> {
  ousToken: string;
}

/**
 * HTTP client for OUS `globalDomain` hosts (`ous-token-v2: <token>`).
 */
export class OusHttpClient extends BaseHttpClient {
  readonly ousToken: string;

  constructor(options: OusClientOptions) {
    super({
      ...options,
      baseUrl: options.baseUrl.replace(/\/$/, ''),
      headers: {
        [OUS_TOKEN_HEADER]: options.ousToken,
      },
    });
    this.ousToken = options.ousToken;
  }
}

export { OUS_TOKEN_HEADER };
