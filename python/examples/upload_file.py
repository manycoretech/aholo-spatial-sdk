#!/usr/bin/env python3
"""Usage: AHOLO_API_KEY=xxx python upload_file.py ./photo.jpg"""
import os
import sys
import time

from manycore.aholo_sdk_asset import create_asset_client
from manycore.aholo_sdk_core import AholoClientConfig

if len(sys.argv) < 2:
    raise SystemExit("Usage: AHOLO_API_KEY=xxx python upload_file.py <file-path>")

file_path = sys.argv[1]
region = os.environ.get("AHOLO_REGION", "cn")
asset = create_asset_client(AholoClientConfig(region=region))

def show_progress(uploaded: int, total: int) -> None:
    pct = int(uploaded / total * 100)
    print(f"\r  {pct}% ({uploaded / 1024 / 1024:.1f}/{total / 1024 / 1024:.1f} MB)", end="", flush=True)

print(f"Uploading {file_path} ...")
t0 = time.monotonic()
result = asset.upload_file(file_path, on_progress=show_progress)
print()  # newline after progress bar
elapsed_ms = int((time.monotonic() - t0) * 1000)
print(f"Upload complete ({elapsed_ms}ms)")
print(f"url: {result.url}")
