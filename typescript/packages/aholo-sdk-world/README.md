# @manycore/aholo-sdk-world

Official TypeScript/Node.js SDK for [Aholo](https://labs.aholo3d.com) World — 3DGS world reconstruction and generation.

**Requirements:** Node.js ≥ 18

## Installation

```bash
npm install @manycore/aholo-sdk-world
```

## Quick Start

```typescript
import { createWorldClient } from '@manycore/aholo-sdk-world';

const world = createWorldClient({ region: 'com' }); // or 'cn'
```

Set `AHOLO_API_KEY` env var, or pass `apiKey` in the config.

### 3DGS Reconstruction (video / Insta360 / images)

Reconstruction accepts `image` (≥20 images), `video` (`.mp4` / `.mov`), or `insv` (Insta360 `.insv`):

```typescript
// Single video
const { worldId } = await world.reconstructions.create({
  name: 'Living room',
  resources: [{ url: 'https://...', type: 'video' }],
  taskQuality: 'normal', // 'low' | 'normal' | 'high'
  scene: 'space',        // 'model' | 'space'
});

// Insta360 panoramic video
await world.reconstructions.create({
  name: 'Panorama room',
  resources: [{ url: 'https://...', type: 'insv' }],
  taskQuality: 'high',
  scene: 'space',
});

const detail = await world.waitFor(worldId);
console.log(detail.assets?.splats?.urls?.plyPath); // PLY download URL
```

### 3DGS Generation (prompt and/or image)

Generation resources support **images only** (`type: 'image'`, at most one). Do not pass `video` or `insv` here.

```typescript
// Prompt only
const { worldId } = await world.generations.create({
  prompt: 'A cozy cabin in the forest',
});

// Prompt + reference image
await world.generations.create({
  prompt: 'Modern minimalist living room',
  resources: [{ url: 'https://...', type: 'image' }],
});

const detail = await world.waitFor(worldId);
console.log(detail.assets?.imagery?.panoUrl); // AI-generated panorama (when available)
```

### Get world detail

```typescript
const detail = await world.retrieve(worldId);
```

### List worlds

```typescript
const list = await world.list({ pageNum: 0, pageSize: 20 });
```

### Input resource types

| API | `type` values | Notes |
|-----|---------------|-------|
| `reconstructions.create` | `image` \| `video` \| `insv` | Images need ≥20 items; `video` / `insv` use a single file |
| `generations.create` | `image` only | At most one image; can combine with `prompt` |

### `WorldDetail` fields

| Field | Type | Description |
|-------|------|-------------|
| `worldId` | `string` | World ID |
| `status` | `string` | `PENDING` \| `PREPROCESSING` \| `RUNNING` \| `SUCCEEDED` \| `FAILED` \| `CANCELED` \| `TIMEOUT` \| `REJECTED` |
| `progress` | `number?` | Reconstruction progress in \[0.0, 1.0\] |
| `assets.splats.urls.plyPath` | `string?` | PLY download URL |
| `assets.splats.urls.spzPath` | `string?` | SPZ download URL |
| `assets.imagery.panoUrl` | `string?` | AI generation panorama URL |

## Examples

Runnable script: [typescript/examples/world-reconstruct.mts](https://github.com/manycoretech/aholo-spatial-sdk/tree/main/typescript/examples/world-reconstruct.mts) (supports `.mp4`, `.mov`, `.insv`).

## Full Documentation

See the [TypeScript SDK README](https://github.com/manycoretech/aholo-spatial-sdk/tree/main/typescript) for complete API reference and examples.

## License

[MIT](https://github.com/manycoretech/aholo-spatial-sdk/blob/main/LICENSE)
