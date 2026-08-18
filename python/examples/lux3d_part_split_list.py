#!/usr/bin/env python3
import sys

from manycore.aholo_sdk_core import AholoClientConfig
from manycore.aholo_sdk_lux3d import create_lux3d_client

if len(sys.argv) < 2:
    raise SystemExit("Usage: AHOLO_API_KEY=xxx python lux3d_part_split_list.py <glb-url>")

lux3d = create_lux3d_client(AholoClientConfig(region="cn"))
task_id = lux3d.part_split.create(glb_url=sys.argv[1])
print("part_split task_id=", task_id)
print("recent tasks=", lux3d.tasks.list(page=1, page_size=20))
