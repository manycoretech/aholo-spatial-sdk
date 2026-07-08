# @manycore/aholo-sdk-lux3d

Official TypeScript/Node.js SDK for [Aholo](https://labs.aholo3d.com) Lux3D — image/text-to-3D generation and material transfer.

**Requirements:** Node.js ≥ 18

## Installation

```bash
npm install @manycore/aholo-sdk-lux3d
```

## Quick Start

```typescript
import { createLux3dClient } from '@manycore/aholo-sdk-lux3d';

const lux3d = createLux3dClient({ region: 'com' }); // or 'cn'
```

Set `AHOLO_API_KEY` env var, or pass `apiKey` in the config.

### Image to 3D

```typescript
const taskId = await lux3d.imgTo3d.create({ img: 'https://example.com/object.jpg' });
const result = await lux3d.tasks.waitFor(taskId);
console.log(result.outputs[0]?.content); // default .zip download URL
```

### Image to 3D (from local file)

```typescript
const taskId = await lux3d.imgTo3d.createFromFile('./object.jpg');
const result = await lux3d.tasks.waitFor(taskId);
```

### v3.0-standard options (face count & optional exports)

```typescript
const taskId = await lux3d.imgTo3d.create({
  img: 'https://example.com/object.jpg',
  faceCount: 80_000,
  needUsdz: true,
  needObj: true,
});
```

### Text to 3D

```typescript
const taskId = await lux3d.textTo3d.create({
  prompt: 'A wooden chair with carved legs',
});
const result = await lux3d.tasks.waitFor(taskId);
```

### Material Transfer

```typescript
const taskId = await lux3d.materialTransfer.create({
  img: 'https://example.com/material.jpg',
  meshUrl: 'https://example.com/model.glb',
});
const result = await lux3d.tasks.waitFor(taskId);
```

### Version differences

| Version | Default | Output formats (`outputs` indices) |
|---------|---------|----------------------------------|
| `v3.0-standard` | ✓ | `[0]` zip, `[1]` glb, `[2]` usdz, `[3]` obj zip, `[4]` fbx zip |
| `v2.0-preview` | | `[0]` zip, `[1]` glb, `[2]` usdz |
| `v1.0-pro` | | `[0]` lux3d only |

For `v3.0-standard`, optional slots (`usdz` / `obj` / `fbx`) return `NOT_REQUESTED` when not requested via `needUsdz` / `needObj` / `needFbx`. `faceCount` (10_000–500_000) applies to v3.0-standard only.

## Full Documentation

See the [TypeScript SDK README](https://github.com/manycoretech/aholo-spatial-sdk/tree/main/typescript) for complete API reference and examples.

## License

[MIT](https://github.com/manycoretech/aholo-spatial-sdk/blob/main/LICENSE)
