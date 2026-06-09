#!/usr/bin/env python3
"""
Minimal example: image-to-3D with Lux3D, poll until model URLs are ready.

v2.0-preview (default): returns .zip, .glb and .usdz formats
v1.0-pro:               returns a single .lux3d format

Usage: AHOLO_API_KEY=xxx python lux3d_img_to_3d.py ./chair.png
"""
import os
import sys
import time

from manycore.aholo_sdk_core import AholoClientConfig
from manycore.aholo_sdk_lux3d import create_lux3d_client

if len(sys.argv) < 2:
    raise SystemExit("Usage: AHOLO_API_KEY=xxx python lux3d_img_to_3d.py <image-path>")

file_path = sys.argv[1]
region = os.environ.get("AHOLO_REGION", "cn")
lux3d = create_lux3d_client(AholoClientConfig(region=region))

print(f"Creating img-to-3D task from {file_path} ...")
task_id = lux3d.img_to_3d.create_from_file(file_path)
print(f"taskId={task_id}, polling...")

t0 = time.monotonic()
result = lux3d.tasks.wait_for(task_id)
print(f"Task complete ({int((time.monotonic() - t0) * 1000)}ms)")
# v2.0-preview outputs: [zip, glb, usdz]
for output in result.get("outputs", []):
    print(output.get("content"))
