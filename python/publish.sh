#!/usr/bin/env bash
set -euo pipefail

# Publish Aholo Python SDK packages to PyPI.
#
# Prerequisites:
#   1. pip install build twine
#   2. PyPI API token configured:
#      - Set TWINE_USERNAME=__token__ and TWINE_PASSWORD=<your-pypi-token>
#      -- or --
#      - Add to ~/.pypirc:
#          [pypi]
#          username = __token__
#          password = pypi-...
#
# Usage:
#   ./publish.sh           # build + publish all packages
#   ./publish.sh --dry-run # build only, skip upload

ROOT="$(cd "$(dirname "$0")" && pwd)"
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
      sed -n '2,16p' "$0"
      exit 0
      ;;
    *) die "Unknown argument: $1" ;;
  esac
done

command -v python3 >/dev/null 2>&1 || die "python3 not found"
python3 -m build --version >/dev/null 2>&1 || die "'build' not installed. Run: pip install build"
if [[ "$DRY_RUN" == false ]]; then
  python3 -m twine --version >/dev/null 2>&1 || die "'twine' not installed. Run: pip install twine"
fi

build_and_publish() {
  local pkg_dir="$ROOT/packages/$1"
  local name
  name="$(python3 -c "import tomllib; f=open('$pkg_dir/pyproject.toml','rb'); d=tomllib.load(f); print(d['project']['name'])")"
  local version
  version="$(python3 -c "import tomllib; f=open('$pkg_dir/pyproject.toml','rb'); d=tomllib.load(f); print(d['project']['version'])")"
  local dist_dir="$pkg_dir/dist"

  log "Building $name==$version ..."
  rm -rf "$dist_dir"
  (cd "$pkg_dir" && python3 -m build)

  if ! compgen -G "$dist_dir/*" >/dev/null; then
    die "No distribution files found in $dist_dir"
  fi

  if [[ "$DRY_RUN" == true ]]; then
    log "Dry-run: skipping upload of $name==$version"
  else
    log "Uploading $name==$version to PyPI ..."
    python3 -m twine upload "$dist_dir"/*
  fi
}

for pkg in "${PACKAGES[@]}"; do
  build_and_publish "$pkg"
done

log "Done."
