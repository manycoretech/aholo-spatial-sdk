#!/usr/bin/env python3
import os
import sys

from manycore.aholo_sdk_core import AholoClientConfig
from manycore.aholo_sdk_lux3d import create_lux3d_client

if len(sys.argv) < 2:
    raise SystemExit("Usage: AHOLO_API_KEY=xxx python lux3d_multi_format_export.py <model-url>")

lux3d = create_lux3d_client(AholoClientConfig(region=os.environ.get("AHOLO_REGION", "cn")))
task_id = lux3d.multi_format_export.create(
    model_url=sys.argv[1], output_format=["usdz", "obj_zip"])
print(lux3d.tasks.wait_for(task_id))
