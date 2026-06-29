#!/usr/bin/env python3
"""Upload media and run a 3DGS reconstruction.

Supported inputs: `.mp4` / `.mov` (type `video`), Insta360 `.insv` (type `insv`).

Usage:
  AHOLO_API_KEY=xxx python world_reconstruct.py ./room.mp4
  AHOLO_API_KEY=xxx python world_reconstruct.py ./panorama.insv
"""
import json
import os
import sys
import time

from manycore.aholo_sdk_asset import create_asset_client
from manycore.aholo_sdk_core import AholoClientConfig
from manycore.aholo_sdk_world import create_world_client


def reconstruction_resource_type(file_path: str) -> str:
    ext = os.path.splitext(file_path)[1].lower()
    if ext == ".insv":
        return "insv"
    if ext in {".mp4", ".mov"}:
        return "video"
    raise SystemExit(f'Unsupported extension "{ext}". Use .mp4, .mov, or .insv')


if len(sys.argv) < 2:
    raise SystemExit("Usage: AHOLO_API_KEY=xxx python world_reconstruct.py <media-path>")

file_path = sys.argv[1]
resource_type = reconstruction_resource_type(file_path)
region = os.environ.get("AHOLO_REGION", "cn")
config = AholoClientConfig(region=region)
asset = create_asset_client(config)
world = create_world_client(config)


def show_progress(uploaded_bytes: int, total: int) -> None:
    pct = int(uploaded_bytes / total * 100)
    print(f"\r  {pct}% ({uploaded_bytes / 1024 / 1024:.1f}/{total / 1024 / 1024:.1f} MB)", end="", flush=True)


print(f"Uploading {file_path} (type={resource_type}) ...")
t0 = time.monotonic()
uploaded = asset.upload_file(file_path, on_progress=show_progress)
print()
print(f"Upload complete ({int((time.monotonic() - t0) * 1000)}ms) url={uploaded.url}")

print("Creating reconstruction task...")
op = world.reconstructions.create(
    resources=[{"url": uploaded.url, "type": resource_type}],
    task_quality="normal",
    scene="model",
    name="SDK reconstruction demo",
)
world_id = op["worldId"]
print(f"worldId={world_id}, polling...")

t0 = time.monotonic()
detail = world.wait_for(world_id)
print(f"Reconstruction complete ({int((time.monotonic() - t0) * 1000)}ms)")
print(json.dumps(detail, indent=2))
