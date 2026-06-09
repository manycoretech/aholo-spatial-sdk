#!/usr/bin/env bash
set -euo pipefail

# Publish Aholo TypeScript SDK packages to npm.
#
# Prerequisites:
#   1. npm login to the public registry (must specify --registry explicitly if
#      your global npm config points to a private registry):
#      npm login --registry=https://registry.npmjs.org/
#      -- or set a token directly --
#      echo "//registry.npmjs.org/:_authToken=${NPM_TOKEN}" >> ~/.npmrc
#   2. @manycore scope publish permission on npmjs.org
#
# Usage:
#   ./publish.sh           # build + publish all packages
#   ./publish.sh --dry-run # build + dry-run only

ROOT="$(cd "$(dirname "$0")" && pwd)"
REGISTRY="https://registry.npmjs.org/"
DRY_RUN=false

PACKAGES=(
  aholo-sdk-core
  aholo-sdk-asset
  aholo-sdk-world
  aholo-sdk-lux3d
)

log() { echo "[publish] $*"; }
die() { echo "[publish] ERROR: $*" >&2; exit 1; }

while [[ $# -gt 0 ]]; do
  case "$1" in
    --dry-run) DRY_RUN=true; shift ;;
    -h|--help)
      sed -n '2,14p' "$0"
      exit 0
      ;;
    *) die "Unknown argument: $1" ;;
  esac
done

if ! npm whoami --registry="$REGISTRY" >/dev/null 2>&1; then
  die "Not logged in to npm. Run 'npm login' or set NPM_TOKEN in .npmrc first."
fi

log "Authenticated as $(npm whoami --registry="$REGISTRY")"
log "Building workspace..."
(cd "$ROOT" && npm run build)

publish_pkg() {
  local dir="$ROOT/packages/$1"
  local name
  name="$(node -p "require('$dir/package.json').name")"
  local version
  version="$(node -p "require('$dir/package.json').version")"

  log "Publishing $name@$version ..."
  if [[ "$DRY_RUN" == true ]]; then
    (cd "$dir" && npm publish --dry-run --access public)
  else
    (cd "$dir" && npm publish --access public)
  fi
}

for pkg in "${PACKAGES[@]}"; do
  publish_pkg "$pkg"
done

log "Done."
