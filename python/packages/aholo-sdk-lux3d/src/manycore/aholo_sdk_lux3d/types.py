from __future__ import annotations

from typing import List, Literal, Optional, TypedDict

Lux3dVersion = Literal["G1", "G1-Turbo"]
MaterialTransferVersion = Literal["v3.0-standard"]
Lux3dStyle = Literal["photorealistic", "cartoon", "anime", "hand_painted", "cyberpunk", "fantasy", "glass"]
Lux3dOutputFormat = Literal["zip", "glb", "ply"]
MaterialTransferOutputFormat = Literal["zip", "glb", "usdz", "obj_zip", "fbx_zip"]
MultiFormatExportOutputFormat = Literal["usdz", "obj_zip", "fbx_zip", "stl", "3mf"]
Lux3dTaskStatus = Literal[0, 1, 3, 4, 6]
# List-filter statuses. Result items may still have status 6 (canceled).
Lux3dTaskListStatus = Literal[0, 1, 3, 4]

LUX3D_STATUS_SUCCESS = 3
LUX3D_STATUS_FAILED = 4
LUX3D_STATUS_CANCELED = 6
LUX3D_OUTPUT_NOT_REQUESTED = "NOT_REQUESTED"


class TaskOutput(TypedDict, total=False):
    content: Optional[str]


class _Lux3dTaskResultBase(TypedDict):
    taskId: int
    status: Lux3dTaskStatus


class Lux3dTaskResult(_Lux3dTaskResultBase, total=False):
    bizId: str
    outputs: List[TaskOutput]


class TaskListItem(TypedDict, total=False):
    taskId: int
    status: Lux3dTaskStatus
    created: int
    lastModified: int


class TaskPagedList(TypedDict, total=False):
    items: List[TaskListItem]
    total: int
    page: int
    pageSize: int
