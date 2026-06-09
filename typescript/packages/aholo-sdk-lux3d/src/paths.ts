import type { AholoClientConfig } from '@manycore/aholo-sdk-core';

const CN_PREFIX = '/lux3d/v1';
const GLOBAL_PREFIX = '/global/lux3d/v1';

export function lux3dPath(region: AholoClientConfig['region'], suffix: string): string {
  const prefix = region === 'com' ? GLOBAL_PREFIX : CN_PREFIX;
  return `${prefix}${suffix.startsWith('/') ? suffix : `/${suffix}`}`;
}
