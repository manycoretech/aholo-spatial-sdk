# @manycore/aholo-sdk-asset

Official TypeScript/Node.js SDK for [Aholo](https://labs.aholo3d.com) — file upload client (single & multipart, resume support).

**Requirements:** Node.js ≥ 18

## Installation

```bash
npm install @manycore/aholo-sdk-asset
```

## Quick Start

```typescript
import { createAssetClient } from '@manycore/aholo-sdk-asset';

const asset = createAssetClient({ region: 'com' }); // or 'cn'
```

Set `AHOLO_API_KEY` env var, or pass `apiKey` in the config.

### Upload a file

```typescript
const result = await asset.uploadFile('./video.mp4');
console.log(result.url); // public URL
```

### Upload a Buffer

```typescript
import { readFileSync } from 'node:fs';

const data = readFileSync('./image.jpg');
const result = await asset.uploadBuffer(data, { filename: 'image.jpg' });
```

### Upload with progress

```typescript
const result = await asset.uploadFile('./video.mp4', {
  onProgress: (uploaded, total) => {
    const pct = Math.round((uploaded / total) * 100);
    process.stdout.write(`\rUploading: ${pct}%`);
  },
});
```

### `UploadOptions`

| Option | Type | Description |
|--------|------|-------------|
| `filename` | `string` | Override filename |
| `onProgress` | `(uploaded: number, total: number) => void` | Progress callback (bytes) |
| `partTimeoutMs` | `number` | Per-block timeout in ms (default: 120 000) |
| `signal` | `AbortSignal` | Cancellation signal |

### `UploadResult`

| Field | Type | Description |
|-------|------|-------------|
| `url` | `string` | Public URL of the uploaded file |
| `md5` | `string` | MD5 of the uploaded file |

## Full Documentation

See the [TypeScript SDK README](https://github.com/manycoretech/aholo-spatial-sdk/tree/main/typescript) for complete API reference and examples.

## License

[MIT](https://github.com/manycoretech/aholo-spatial-sdk/blob/main/LICENSE)
