# @manycore/aholo-sdk-lux3d

Official TypeScript/Node.js SDK for Aholo Lux3D generation, material transfer, multi-format export, and task history.

**Requirements:** Node.js ≥ 18

## Installation

```bash
npm install @manycore/aholo-sdk-lux3d
```

```typescript
import { createLux3dClient } from '@manycore/aholo-sdk-lux3d';

const lux3d = createLux3dClient({ region: 'com' }); // or 'cn'
```

Set `AHOLO_API_KEY`, or pass `apiKey` in the client config.

## Multimodal to image

Create a single image from a prompt, a reference image, or both:

```typescript
await lux3d.multimodalToImage.create({ prompt: 'A light wood dining chair on a white background' });
await lux3d.multimodalToImage.createFromFile('./object.jpg', { prompt: 'Keep the viewpoint' });
```

## Image to four views

`img` and/or `prompt` are accepted. Prompt-only generation is valid:

```typescript
const taskId = await lux3d.imageToFourView.create({
  img: 'https://example.com/object.jpg',
});
await lux3d.imageToFourView.create({ prompt: 'A light wood dining chair' });
await lux3d.imageToFourView.createFromFile('./object.jpg');
```

## Image to 3D

```typescript
const taskId = await lux3d.imgTo3d.create({
  img: 'https://example.com/object.jpg',
  version: 'G1', // or G1-Turbo
  faceCount: 200_000,
  outputFormat: ['zip', 'glb', 'ply'],
  enablePbr: true,
  aiPredictSize: true,
});
```

For local files, the required version is passed separately:

```typescript
await lux3d.imgTo3d.createFromFile('./object.jpg', { version: 'G1' });
await lux3d.imgTo3d.createFromFiles(
  ['./view1.png', './view2.png'],
  { version: 'G1-Turbo', outputFormat: ['glb'] },
);
```

## Text to 3D

```typescript
await lux3d.textTo3d.create({
  prompt: 'A wooden chair with carved legs',
  version: 'G1-Turbo',
  outputFormat: ['glb'],
});
```

## Material transfer

```typescript
await lux3d.materialTransfer.create({
  img: 'https://example.com/material.jpg',
  meshUrl: 'https://example.com/model.glb',
  version: 'v3.0-standard',
  outputFormat: ['zip', 'glb', 'usdz'],
  aiPredictSize: false,
  customSize: 120,
});
```

## Multi-format export

```typescript
await lux3d.multiFormatExport.create({
  modelUrl: 'https://example.com/model.glb',
  outputFormat: ['usdz', 'obj_zip', 'fbx_zip', 'stl', '3mf'],
});
```

## Tasks

```typescript
const result = await lux3d.tasks.retrieve(taskId);
const page = await lux3d.tasks.list({ status: 3 });
const completed = await lux3d.tasks.waitFor(taskId);
```

List filters accept `0` init, `1` running, `3` success, and `4` failed. Result items may still have `6` canceled. Polling rejects on failed or canceled tasks.

## License

[MIT](https://github.com/manycoretech/aholo-spatial-sdk/blob/main/LICENSE)
