import { createHash } from 'node:crypto';

/** MD5 hex digest (lowercase) for OUS upload APIs. */
export function md5Hex(data: Buffer): string {
  return createHash('md5').update(data).digest('hex');
}
