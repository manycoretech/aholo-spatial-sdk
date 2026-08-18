import { createLux3dClient } from '@manycore/aholo-sdk-lux3d';

const lux3d = createLux3dClient({ region: (process.env.AHOLO_REGION as 'cn' | 'com') || 'cn' });
const glbUrl = process.argv[2];
if (!glbUrl) throw new Error('Usage: AHOLO_API_KEY=xxx npx tsx examples/lux3d-part-split-list.mts <glb-url>');

const taskId = await lux3d.partSplit.create({ glbUrl });
console.log('partSplit taskId=', taskId);
console.log('recent tasks=', await lux3d.tasks.list({ page: 1, pageSize: 20 }));
