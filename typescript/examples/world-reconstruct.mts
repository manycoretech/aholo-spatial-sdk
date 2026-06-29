/**
 * Minimal example: upload media, create a 3DGS reconstruction, poll until done.
 *
 * Supported inputs: `.mp4` / `.mov` (type `video`), Insta360 `.insv` (type `insv`).
 *
 * Usage:
 *   AHOLO_API_KEY=xxx npx tsx examples/world-reconstruct.mts ./room.mp4
 *   AHOLO_API_KEY=xxx npx tsx examples/world-reconstruct.mts ./panorama.insv
 */
import path from 'node:path';

import { createAssetClient } from '@manycore/aholo-sdk-asset';
import { createWorldClient, type WorldResourceType } from '@manycore/aholo-sdk-world';

function reconstructionResourceType(filePath: string): WorldResourceType {
  const ext = path.extname(filePath).toLowerCase();
  if (ext === '.insv') return 'insv';
  if (ext === '.mp4' || ext === '.mov') return 'video';
  console.error(`Unsupported extension "${ext}". Use .mp4, .mov, or .insv`);
  process.exit(1);
}

const filePath = process.argv[2];
if (!filePath) {
  console.error('Usage: AHOLO_API_KEY=xxx npx tsx examples/world-reconstruct.mts <media-path>');
  process.exit(1);
}

const resourceType = reconstructionResourceType(filePath);

const region = (process.env.AHOLO_REGION ?? 'cn') as 'cn' | 'com';
const config = { region };
const asset = createAssetClient(config);
const world = createWorldClient(config);

console.log(`Uploading ${filePath} (type=${resourceType}) ...`);
let t0 = Date.now();
const uploaded = await asset.uploadFile(filePath, {
  onProgress(uploadedBytes, total) {
    const pct = Math.round((uploadedBytes / total) * 100);
    process.stdout.write(`\r  ${pct}% (${(uploadedBytes / 1024 / 1024).toFixed(1)}/${(total / 1024 / 1024).toFixed(1)} MB)`);
  },
});
process.stdout.write('\n');
console.log(`Upload complete (${Date.now() - t0}ms) url=${uploaded.url}`);

console.log('Creating reconstruction task...');
const { worldId } = await world.reconstructions.create({
  name: 'SDK reconstruction demo',
  resources: [{ url: uploaded.url, type: resourceType }],
  taskQuality: 'normal',
  scene: 'model',
});
console.log(`worldId=${worldId}, polling...`);

t0 = Date.now();
const detail = await world.waitFor(worldId);
console.log(`Reconstruction complete (${Date.now() - t0}ms)`);
console.log(JSON.stringify(detail, null, 2));
