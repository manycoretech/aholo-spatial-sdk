import assert from 'node:assert/strict';
import test from 'node:test';

import type { AholoGatewayClient } from '@manycore/aholo-sdk-core';

import { PartSplitResource } from '../src/resources/part-split.js';
import { TasksResource } from '../src/resources/tasks.js';

test('part split uses the expected endpoint and returns the task id', async () => {
  let request: unknown;
  const gateway = {
    gatewayRequest: async (options: unknown) => {
      request = options;
      return { c: '0', m: '', d: 42 };
    },
  } as AholoGatewayClient;

  const resource = new PartSplitResource(gateway, 'cn');
  assert.equal(await resource.create({ glbUrl: 'https://example.com/model.glb' }), 42);
  assert.deepEqual(request, {
    method: 'POST',
    path: '/lux3d/v1/part-split/task/create',
    body: { glbUrl: 'https://example.com/model.glb' },
    signal: undefined,
  });
});

test('task list maps camelCase filters and omits unset values', async () => {
  const requests: unknown[] = [];
  const gateway = {
    gatewayRequest: async (options: unknown) => {
      requests.push(options);
      return { c: '0', m: '', d: { items: [], total: 0, page: 2, pageSize: 10 } };
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

  await resource.list();
  assert.deepEqual(requests[1], {
    method: 'GET',
    path: '/global/lux3d/v1/generate/task/list',
    query: {},
    signal: undefined,
  });
});
