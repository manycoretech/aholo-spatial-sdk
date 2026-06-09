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

### 3DGS Reconstruction (from video / images)

```typescript
const { worldId } = await world.reconstructions.create({
  name: 'Living room',
  resources: [{ url: 'https://...', type: 'video' }],
  taskQuality: 'normal', // 'low' | 'normal' | 'high'
  scene: 'model',        // 'model' | 'space'
});

const detail = await world.waitFor(worldId);
console.log(detail.assets?.splats?.urls?.plyPath); // PLY download URL
```

### 3DGS Generation (from prompt)

```typescript
const { worldId } = await world.generations.create({
  prompt: 'A cozy cabin in the forest',
});
const detail = await world.waitFor(worldId);
```

### Get world detail

```typescript
const detail = await world.retrieve(worldId);
```

### List worlds

```typescript
const list = await world.list({ pageNum: 1, pageSize: 20 });
```

### `WorldDetail` fields

| Field | Type | Description |
|-------|------|-------------|
| `worldId` | `string` | World ID |
| `status` | `string` | `PENDING` \| `PREPROCESSING` \| `RUNNING` \| `SUCCEEDED` \| `FAILED` \| `CANCELED` \| `TIMEOUT` \| `REJECTED` |
| `progress` | `number?` | Reconstruction progress in \[0.0, 1.0\] |
| `assets.splats.urls.plyPath` | `string?` | PLY download URL |
| `assets.splats.urls.spzPath` | `string?` | SPZ download URL |

## Full Documentation

See the [TypeScript SDK README](https://github.com/manycoretech/aholo-spatial-sdk/tree/main/typescript) for complete API reference and examples.

## License

[MIT](https://github.com/manycoretech/aholo-spatial-sdk/blob/main/LICENSE)
