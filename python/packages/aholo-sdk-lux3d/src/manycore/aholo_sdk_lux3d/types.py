from __future__ import annotations

from typing import List, Literal, Optional, TypedDict

Lux3dVersion = Literal["v3.0-standard", "v2.0-preview", "v1.0-pro", "G1"]
Lux3dStyle = Literal["photorealistic", "cartoon", "anime", "hand_painted", "cyberpunk", "fantasy", "glass"]
Lux3dOutputFormat = Literal["zip", "glb", "usdz", "obj_zip", "fbx_zip", "ply"]
Lux3dTaskStatus = int  # 0=init, 1=running, 3=success, 4=failed

LUX3D_STATUS_SUCCESS = 3
LUX3D_STATUS_FAILED = 4
LUX3D_OUTPUT_NOT_REQUESTED = "NOT_REQUESTED"


class TaskOutput(TypedDict, total=False):
    content: Optional[str]


class _Lux3dTaskResultBase(TypedDict):
    taskId: int
    status: int

class Lux3dTaskResult(_Lux3dTaskResultBase, total=False):
    outputs: List[TaskOutput]
