# @manycore/aholo-sdk-core

Shared HTTP client, authentication, error types, and polling utilities for the [Aholo](https://labs.aholo3d.com) TypeScript SDKs.

> This package is a peer dependency used internally by `@manycore/aholo-sdk-world`, `@manycore/aholo-sdk-lux3d`, and `@manycore/aholo-sdk-asset`. You typically do not need to install it directly.

**Requirements:** Node.js ≥ 18

## Installation

```bash
npm install @manycore/aholo-sdk-core
```

## Error Types

```typescript
import {
  AuthenticationError,
  RateLimitError,
  BusinessError,
  PollingTimeoutError,
  PollingFailedError,
} from '@manycore/aholo-sdk-core';

try {
  const detail = await world.waitFor(worldId);
} catch (e) {
  if (e instanceof AuthenticationError) {
    console.error('Invalid or missing API Key');
  } else if (e instanceof RateLimitError) {
    console.error('Rate limit exceeded');
  } else if (e instanceof BusinessError) {
    console.error('API error:', e.code, e.message);
  } else if (e instanceof PollingTimeoutError) {
    console.error('Task timed out after 10 minutes');
  } else if (e instanceof PollingFailedError) {
    console.error('Task failed:', e.message);
  }
}
```

## `AholoClientConfig`

| Field | Type | Default | Description |
|-------|------|---------|-------------|
| `apiKey` | `string` | `AHOLO_API_KEY` env var | API key |
| `region` | `'cn' \| 'com'` | `'cn'` | `cn` = China, `com` = Global |
| `timeoutMs` | `number` | `30000` | Request timeout |

## Full Documentation

See the [TypeScript SDK README](https://github.com/manycoretech/aholo-spatial-sdk/tree/main/typescript) for complete API reference and examples.

## License

[MIT](https://github.com/manycoretech/aholo-spatial-sdk/blob/main/LICENSE)
