#!/usr/bin/env bash
# Local development helper for this repository only.
# End users should install from PyPI instead, e.g.:
#   pip install manycore-aholo-sdk-asset
ROOT="$(cd "$(dirname "$0")" && pwd)"
export PYTHONPATH="$ROOT/packages/aholo-sdk-core/src:$ROOT/packages/aholo-sdk-asset/src:$ROOT/packages/aholo-sdk-world/src:$ROOT/packages/aholo-sdk-lux3d/src:${PYTHONPATH:-}"
python3 -m pip install --user httpx >/dev/null 2>&1 || true
echo "Aholo Python SDK ready. Use: source $ROOT/install.sh"
echo "Example: AHOLO_API_KEY=xxx python3 $ROOT/examples/upload_file.py ./photo.jpg"
