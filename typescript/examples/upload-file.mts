/**
 * Minimal example: upload a local file via OUS and print the public URL.
 *
 * Usage:
 *   AHOLO_API_KEY=xxx npx tsx examples/upload-file.mts ./photo.jpg
 */
import { createAssetClient } from '@manycore/aholo-sdk-asset';

const filePath = process.argv[2];
if (!filePath) {
  console.error('Usage: AHOLO_API_KEY=xxx npx tsx examples/upload-file.mts <file-path>');
  process.exit(1);
}

const region = (process.env.AHOLO_REGION ?? 'cn') as 'cn' | 'com';
const asset = createAssetClient({ region });

console.log(`Uploading ${filePath} ...`);
const t0 = Date.now();
const result = await asset.uploadFile(filePath, {
  onProgress(uploaded, total) {
    const pct = Math.round((uploaded / total) * 100);
    process.stdout.write(`\r  ${pct}% (${(uploaded / 1024 / 1024).toFixed(1)}/${(total / 1024 / 1024).toFixed(1)} MB)`);
  },
});
process.stdout.write('\n');
console.log(`Upload complete (${Date.now() - t0}ms)`);
console.log(`url: ${result.url}`);
