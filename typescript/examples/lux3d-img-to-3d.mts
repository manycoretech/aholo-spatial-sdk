/**
 * Minimal example: image-to-3D with Lux3D, poll until the model URLs are ready.
 *
 * v3.0-standard / v2.0-preview: zip + glb + optional usdz/obj/fbx via outputFormat
 * v1.0-pro:                     single ZIP
 * G1:                           zip / glb / ply via outputFormat
 *
 * Usage:
 *   AHOLO_API_KEY=xxx npx tsx examples/lux3d-img-to-3d.mts ./chair.png
 */
import { createLux3dClient } from '@manycore/aholo-sdk-lux3d';

const filePath = process.argv[2];
if (!filePath) {
  console.error('Usage: AHOLO_API_KEY=xxx npx tsx examples/lux3d-img-to-3d.mts <image-path>');
  process.exit(1);
}

const region = (process.env.AHOLO_REGION ?? 'cn') as 'cn' | 'com';
const lux3d = createLux3dClient({ region });

console.log(`Creating img-to-3D task from ${filePath} ...`);
const taskId = await lux3d.imgTo3d.createFromFile(filePath);
console.log(`taskId=${taskId}, polling...`);

const t0 = Date.now();
const result = await lux3d.tasks.waitFor(taskId);
console.log(`Task complete (${Date.now() - t0}ms)`);
// Optional slots may be NOT_REQUESTED when not listed in outputFormat
for (const output of result.outputs) {
  console.log(output.content);
}
