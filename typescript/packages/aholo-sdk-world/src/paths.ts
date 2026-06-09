import type { AholoClientConfig } from '@manycore/aholo-sdk-core';

const CN_PREFIX = '/world/v1';
const GLOBAL_PREFIX = '/global/world/v1';

export function worldPath(region: AholoClientConfig['region'], suffix: string): string {
  const prefix = region === 'com' ? GLOBAL_PREFIX : CN_PREFIX;
  return `${prefix}${suffix.startsWith('/') ? suffix : `/${suffix}`}`;
}
