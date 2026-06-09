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
console.log(result.outputs[0]?.content); // download URL
```

### Image to 3D (from local file)

```typescript
const taskId = await lux3d.imgTo3d.createFromFile('./object.jpg');
const result = await lux3d.tasks.waitFor(taskId);
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

| Version | Default | Output formats |
|---------|---------|----------------|
| `v2.0-preview` | ✓ | `.zip` + `.glb` + `.usdz` |
| `v1.0-pro` | | `.lux3d` |

## Full Documentation

See the [TypeScript SDK README](https://github.com/manycoretech/aholo-spatial-sdk/tree/main/typescript) for complete API reference and examples.

## License

[MIT](https://github.com/manycoretech/aholo-spatial-sdk/blob/main/LICENSE)
