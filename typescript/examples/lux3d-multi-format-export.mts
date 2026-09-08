import { createLux3dClient } from '@manycore/aholo-sdk-lux3d';

const modelUrl = process.argv[2];
if (!modelUrl) throw new Error('Usage: AHOLO_API_KEY=xxx npx tsx examples/lux3d-multi-format-export.mts <model-url>');

const lux3d = createLux3dClient({ region: (process.env.AHOLO_REGION ?? 'cn') as 'cn' | 'com' });
const taskId = await lux3d.multiFormatExport.create({ modelUrl, outputFormat: ['usdz', 'obj_zip', 'stl', '3mf'] });
console.log('taskId=', taskId);
console.log(await lux3d.tasks.waitFor(taskId));
