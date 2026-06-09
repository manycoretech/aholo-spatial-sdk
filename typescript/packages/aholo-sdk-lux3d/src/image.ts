import { extname } from 'node:path';

const MIME_BY_EXT: Record<string, string> = {
  '.jpg': 'image/jpeg',
  '.jpeg': 'image/jpeg',
  '.png': 'image/png',
  '.webp': 'image/webp',
  '.gif': 'image/gif',
};

export function guessMimeType(filePath: string, fallback = 'application/octet-stream'): string {
  return MIME_BY_EXT[extname(filePath).toLowerCase()] ?? fallback;
}

/** Encode bytes as a Data URL (`data:image/png;base64,...`). */
export function bufferToDataUrl(data: Buffer, mimeType: string): string {
  return `data:${mimeType};base64,${data.toString('base64')}`;
}

/** Read a local image and return a Data URL suitable for Lux3D `img` fields. */
export async function fileToDataUrl(filePath: string, mimeType?: string): Promise<string> {
  const { readFile } = await import('node:fs/promises');
  const data = await readFile(filePath);
  return bufferToDataUrl(data, mimeType ?? guessMimeType(filePath));
}
