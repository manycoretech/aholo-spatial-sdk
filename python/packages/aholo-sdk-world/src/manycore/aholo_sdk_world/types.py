from __future__ import annotations

from typing import List, Literal, TypedDict

WorldTaskStatus = Literal["PENDING", "PREPROCESSING", "RUNNING", "SUCCEEDED", "FAILED", "CANCELED", "TIMEOUT", "REJECTED"]
WorldScene = Literal["model", "space"]
WorldTaskQuality = Literal["low", "normal", "high"]
WorldResourceType = Literal["image", "video", "insv"]
WorldGenResourceType = Literal["image"]
WorldUpAxis = Literal["Y", "Z"]

WORLD_TERMINAL_FAILURE_STATUSES = frozenset({"FAILED", "CANCELED", "TIMEOUT", "REJECTED"})


# Two-class pattern for required + optional fields (Python < 3.11 compatible)

class _WorldResourceItemBase(TypedDict):
    url: str

class WorldResourceItem(_WorldResourceItemBase, total=False):
    type: WorldResourceType


class _GenerateWorldResourceItemBase(TypedDict):
    url: str

class GenerateWorldResourceItem(_GenerateWorldResourceItemBase, total=False):
    type: WorldGenResourceType


class _WorldAsyncOperationBase(TypedDict):
    worldId: str

class WorldAsyncOperation(_WorldAsyncOperationBase, total=False):
    pass


class SplatFileUrls(TypedDict, total=False):
    plyPath: str
    spzPath: str
    lodMetaPath: str


class WorldSplatBundle(TypedDict, total=False):
    urls: SplatFileUrls


class WorldImagery(TypedDict, total=False):
    panoUrl: str


class WorldSemanticsMetadata(TypedDict, total=False):
    upAxis: WorldUpAxis


class WorldAssetBundle(TypedDict, total=False):
    splats: WorldSplatBundle
    imagery: WorldImagery
    semanticsMetadata: WorldSemanticsMetadata


class _WorldDetailBase(TypedDict):
    worldId: str

class WorldDetail(_WorldDetailBase, total=False):
    name: str
    cover: str
    scene: WorldScene
    createTime: int
    updateTime: int
    status: WorldTaskStatus
    progress: float
    assets: WorldAssetBundle


class WorldPagedList(TypedDict, total=False):
    pageNum: int
    pageSize: int
    count: int
    totalCount: int
    hasMore: bool
    result: List[WorldDetail]
