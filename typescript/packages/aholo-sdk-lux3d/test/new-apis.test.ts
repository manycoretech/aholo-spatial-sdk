import assert from 'node:assert/strict';
import test from 'node:test';

import type { AholoGatewayClient } from '@manycore/aholo-sdk-core';

import { ImageToFourViewResource } from '../src/resources/image-to-four-view.js';
import { ImgTo3dResource } from '../src/resources/img-to-3d.js';
import { MultimodalToImageResource } from '../src/resources/multimodal-to-image.js';
import { MultiFormatExportResource } from '../src/resources/multi-format-export.js';
import { TasksResource } from '../src/resources/tasks.js';

function taskGateway(requests: unknown[]): AholoGatewayClient {
  return {
    gatewayRequest: async (options: unknown) => {
      requests.push(options);
      return { c: '0', m: '', d: 42, f: null };
    },
  } as AholoGatewayClient;
}

test('image to four view uses the expected endpoint', async () => {
  const requests: unknown[] = [];
  const resource = new ImageToFourViewResource(taskGateway(requests), 'cn');
  assert.equal(await resource.create({ img: 'https://example.com/image.png' }), 42);
  assert.deepEqual(requests[0], {
    method: 'POST',
    path: '/lux3d/v1/generate/image-to-four-view/task/create',
    body: { img: 'https://example.com/image.png' },
    signal: undefined,
  });
});

test('image to four view accepts prompt-only and image+prompt bodies', async () => {
  const requests: unknown[] = [];
  const resource = new ImageToFourViewResource(taskGateway(requests), 'cn');
  assert.equal(await resource.create({ prompt: 'a wooden dining chair' }), 42);
  assert.equal(
    await resource.create({ img: 'https://example.com/image.png', prompt: 'keep the viewpoint' }),
    42,
  );
  assert.deepEqual(requests[0], {
    method: 'POST',
    path: '/lux3d/v1/generate/image-to-four-view/task/create',
    body: { prompt: 'a wooden dining chair' },
    signal: undefined,
  });
});

test('multimodal to image posts prompt-only to the China endpoint', async () => {
  const requests: unknown[] = [];
  const resource = new MultimodalToImageResource(taskGateway(requests), 'cn');
  assert.equal(await resource.create({ prompt: 'a wooden dining chair' }), 42);
  assert.deepEqual(requests[0], {
    method: 'POST',
    path: '/lux3d/v1/generate/multimodal-to-image/task/create',
    body: { prompt: 'a wooden dining chair' },
    signal: undefined,
  });
});

test('multimodal to image rejects an empty body', async () => {
  const resource = new MultimodalToImageResource(taskGateway([]), 'cn');
  await assert.rejects(
    () => resource.create({} as never),
    /at least one of img or prompt/,
  );
});

test('multi format export uses the expected global endpoint', async () => {
  const requests: unknown[] = [];
  const resource = new MultiFormatExportResource(taskGateway(requests), 'com');
  const body = { modelUrl: 'https://example.com/model.glb', outputFormat: ['usdz'] as const };
  assert.equal(await resource.create(body), 42);
  assert.deepEqual(requests[0], {
    method: 'POST',
    path: '/global/lux3d/v1/multi-format-export/task/create',
    body,
    signal: undefined,
  });
});

test('multi format export accepts stl and 3mf', async () => {
  const requests: unknown[] = [];
  const resource = new MultiFormatExportResource(taskGateway(requests), 'cn');
  const body = { modelUrl: 'https://example.com/model.glb', outputFormat: ['stl', '3mf'] as const };
  assert.equal(await resource.create(body), 42);
  assert.deepEqual((requests[0] as { body: unknown }).body, body);
});

test('image to 3D enforces exactly one image input at runtime', async () => {
  const resource = new ImgTo3dResource(taskGateway([]), 'cn');
  await assert.rejects(
    () => resource.create({ version: 'G1' } as never),
    /exactly one of img or imgs/,
  );
  await assert.rejects(
    () => resource.create({ img: 'a', imgs: ['b'], version: 'G1' } as never),
    /exactly one of img or imgs/,
  );
});

test('task list maps filters without canceled status', async () => {
  const requests: unknown[] = [];
  const gateway = {
    gatewayRequest: async (options: unknown) => {
      requests.push(options);
      return { c: '0', m: '', d: { items: [], total: 0, page: 2, pageSize: 10 }, f: null };
    },
  } as AholoGatewayClient;

  const resource = new TasksResource(gateway, 'com');
  const result = await resource.list({ page: 2, pageSize: 10, status: 3, startTime: 100, endTime: 200 });
  assert.equal(result.total, 0);
  assert.deepEqual(requests[0], {
    method: 'GET',
    path: '/global/lux3d/v1/generate/task/list',
    query: { page: '2', pagesize: '10', status: '3', starttime: '100', endtime: '200' },
    signal: undefined,
  });
});

test('task retrieval includes bizId and canceled status ends polling', async () => {
  const gateway = {
    gatewayRequest: async () => ({
      c: '0',
      m: '',
      d: { taskId: 42, bizId: 'LUX_3D', status: 6, outputs: [] },
      f: null,
    }),
  } as AholoGatewayClient;
  const resource = new TasksResource(gateway, 'cn');
  assert.deepEqual(await resource.retrieve(42), {
    taskId: 42,
    bizId: 'LUX_3D',
    status: 6,
    outputs: [],
  });
  await assert.rejects(() => resource.waitFor(42), /status=6/);
});
