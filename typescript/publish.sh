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
#   ./publish.sh                    # build + publish all packages
#   ./publish.sh --only world       # publish @manycore/aholo-sdk-world only
#   ./publish.sh --only aholo-sdk-world
#   ./publish.sh --dry-run
#   ./publish.sh --only world --dry-run

ROOT="$(cd "$(dirname "$0")" && pwd)"
REGISTRY="https://registry.npmjs.org/"
DRY_RUN=false
ONLY_FILTER=()

PACKAGES=(
  aholo-sdk-core
  aholo-sdk-asset
  aholo-sdk-world
  aholo-sdk-lux3d
)

log() { echo "[publish] $*"; }
die() { echo "[publish] ERROR: $*" >&2; exit 1; }

resolve_package() {
  case "$1" in
    core|aholo-sdk-core) echo "aholo-sdk-core" ;;
    asset|aholo-sdk-asset) echo "aholo-sdk-asset" ;;
    world|aholo-sdk-world) echo "aholo-sdk-world" ;;
    lux3d|aholo-sdk-lux3d) echo "aholo-sdk-lux3d" ;;
    *) die "Unknown package: $1 (use core|asset|world|lux3d or aholo-sdk-*)" ;;
  esac
}

while [[ $# -gt 0 ]]; do
  case "$1" in
    --dry-run) DRY_RUN=true; shift ;;
    --only)
      [[ $# -ge 2 ]] || die "--only requires a package name (e.g. world)"
      ONLY_FILTER+=("$(resolve_package "$2")")
      shift 2
      ;;
    -h|--help)
      sed -n '2,20p' "$0"
      exit 0
      ;;
    *) die "Unknown argument: $1" ;;
  esac
done

if ((${#ONLY_FILTER[@]} > 0)); then
  PACKAGES=("${ONLY_FILTER[@]}")
  log "Selected packages: ${PACKAGES[*]}"
fi

if ! npm whoami --registry="$REGISTRY" >/dev/null 2>&1; then
  die "Not logged in to npm. Run 'npm login' or set NPM_TOKEN in .npmrc first."
fi

log "Authenticated as $(npm whoami --registry="$REGISTRY")"
log "Building workspace..."
build_pkgs=()
needs_core=false
for pkg in "${PACKAGES[@]}"; do
  build_pkgs+=("$pkg")
  [[ "$pkg" != "aholo-sdk-core" ]] && needs_core=true
done
if [[ "$needs_core" == true ]]; then
  for pkg in "${build_pkgs[@]}"; do
    if [[ "$pkg" != "aholo-sdk-core" ]]; then
      (cd "$ROOT" && npm run build -w "@manycore/aholo-sdk-core")
      break
    fi
  done
fi
for pkg in "${build_pkgs[@]}"; do
  (cd "$ROOT" && npm run build -w "@manycore/$pkg")
done

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
