#!/usr/bin/env python3
"""Usage: AHOLO_API_KEY=xxx python world_reconstruct.py ./room.mp4"""
import json
import os
import sys
import time

from manycore.aholo_sdk_asset import create_asset_client
from manycore.aholo_sdk_core import AholoClientConfig
from manycore.aholo_sdk_world import create_world_client

if len(sys.argv) < 2:
    raise SystemExit("Usage: AHOLO_API_KEY=xxx python world_reconstruct.py <video-path>")

file_path = sys.argv[1]
region = os.environ.get("AHOLO_REGION", "cn")
config = AholoClientConfig(region=region)
asset = create_asset_client(config)
world = create_world_client(config)


def show_progress(uploaded_bytes: int, total: int) -> None:
    pct = int(uploaded_bytes / total * 100)
    print(f"\r  {pct}% ({uploaded_bytes / 1024 / 1024:.1f}/{total / 1024 / 1024:.1f} MB)", end="", flush=True)


print(f"Uploading {file_path} ...")
t0 = time.monotonic()
uploaded = asset.upload_file(file_path, on_progress=show_progress)
print()
print(f"Upload complete ({int((time.monotonic() - t0) * 1000)}ms) url={uploaded.url}")

print("Creating reconstruction task...")
op = world.reconstructions.create(
    resources=[{"url": uploaded.url, "type": "video"}],
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
