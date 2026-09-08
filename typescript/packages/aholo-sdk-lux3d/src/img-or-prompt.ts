function isNonBlank(value: string | undefined): value is string {
  return typeof value === 'string' && value.trim() !== '';
}

/** At least one of `img` or `prompt` must be a non-blank string. */
export function requireImgOrPrompt(
  body: { img?: string; prompt?: string },
  context: string,
): void {
  if (!isNonBlank(body.img) && !isNonBlank(body.prompt)) {
    throw new TypeError(`${context}: provide at least one of img or prompt`);
  }
}
