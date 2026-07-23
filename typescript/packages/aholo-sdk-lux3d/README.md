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

### Output formats & face count

```typescript
const taskId = await lux3d.imgTo3d.create({
  img: 'https://example.com/object.jpg',
  faceCount: 80_000,
  outputFormat: ['zip', 'glb', 'usdz', 'obj_zip'],
});
```

### G1 multi-view (local files)

```typescript
const taskId = await lux3d.imgTo3d.createFromFiles(
  ['./view1.png', './view2.png', './view3.png'],
  { version: 'G1', outputFormat: ['glb'], enablePbr: true },
);
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
  outputFormat: ['zip', 'glb'],
});
const result = await lux3d.tasks.waitFor(taskId);
```

### Version differences

| Version | Default | Outputs |
|---------|---------|---------|
| `v3.0-standard` | ✓ | Five slots: zip / glb / usdz / obj_zip / fbx_zip; unrequested → `NOT_REQUESTED` |
| `v2.0-preview` | | Same five-slot layout as v3 |
| `v1.0-pro` | | Single ZIP |
| `G1` (beta) | | `results.zip` / `tex_mesh.glb`\|`mesh.glb` / `gaussian.ply` via `outputFormat`; `enablePbr` / `textureSize` |

Use `outputFormat` (string array) instead of the removed `needUsdz` / `needObj` / `needFbx`. `faceCount` (10_000–500_000) applies to v2 / v3 / G1; v1 ignores it.

## Full Documentation

See the [TypeScript SDK README](https://github.com/manycoretech/aholo-spatial-sdk/tree/main/typescript) for complete API reference and examples.

## License

[MIT](https://github.com/manycoretech/aholo-spatial-sdk/blob/main/LICENSE)
