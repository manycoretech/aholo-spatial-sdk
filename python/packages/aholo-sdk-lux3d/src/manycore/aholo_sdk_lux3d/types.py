from __future__ import annotations

from typing import List, Literal, Optional, TypedDict

Lux3dVersion = Literal["v2.0-preview", "v1.0-pro"]
Lux3dStyle = Literal["photorealistic", "cartoon", "anime", "hand_painted", "cyberpunk", "fantasy", "glass"]
Lux3dTaskStatus = int  # 0=init, 1=running, 3=success, 4=failed

LUX3D_STATUS_SUCCESS = 3
LUX3D_STATUS_FAILED = 4


class TaskOutput(TypedDict, total=False):
    content: Optional[str]


class _Lux3dTaskResultBase(TypedDict):
    taskId: int
    status: int

class Lux3dTaskResult(_Lux3dTaskResultBase, total=False):
    outputs: List[TaskOutput]
