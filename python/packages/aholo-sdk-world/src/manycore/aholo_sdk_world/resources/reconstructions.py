from __future__ import annotations

from typing import TYPE_CHECKING, List, Optional, cast

from .._paths import world_path
from ..types import WorldAsyncOperation, WorldResourceItem, WorldScene, WorldTaskQuality

if TYPE_CHECKING:
    from manycore.aholo_sdk_core import AholoGatewayClient


class ReconstructionsResource:
    def __init__(self, gateway: AholoGatewayClient, region: str) -> None:
        self._gateway = gateway
        self._region = region

    def create(
        self,
        *,
        resources: List[WorldResourceItem],
        task_quality: WorldTaskQuality,
        scene: WorldScene,
        name: Optional[str] = None,
        cover: Optional[str] = None,
        use_mask: Optional[bool] = None,
        x_source: Optional[str] = None,
    ) -> WorldAsyncOperation:
        """POST /world/v1/reconstructions — Create a 3DGS reconstruction job."""
        body: dict = {"resources": resources, "taskQuality": task_quality, "scene": scene}
        if name is not None:
            body["name"] = name
        if cover is not None:
            body["cover"] = cover
        if use_mask is not None:
            body["useMask"] = use_mask
        headers = {"x-source": x_source} if x_source else None
        return cast(
            WorldAsyncOperation,
            self._gateway.gateway_request(
                method="POST",
                path=world_path(self._region, "/reconstructions"),
                body=body,
                headers=headers,
            ),
        )

from manycore.aholo_sdk_core import AsyncAholoGatewayClient


class AsyncReconstructionsResource:
    def __init__(self, gateway: AsyncAholoGatewayClient, region: str) -> None:
        self._gateway = gateway
        self._region = region

    async def create(
        self,
        *,
        resources: List[WorldResourceItem],
        task_quality: WorldTaskQuality,
        scene: WorldScene,
        name: Optional[str] = None,
        cover: Optional[str] = None,
        use_mask: Optional[bool] = None,
        x_source: Optional[str] = None,
    ) -> WorldAsyncOperation:
        """POST /world/v1/reconstructions — Create a 3DGS reconstruction job."""
        body: dict = {"resources": resources, "taskQuality": task_quality, "scene": scene}
        if name is not None:
            body["name"] = name
        if cover is not None:
            body["cover"] = cover
        if use_mask is not None:
            body["useMask"] = use_mask
        headers = {"x-source": x_source} if x_source else None
        return cast(
            WorldAsyncOperation,
            await self._gateway.gateway_request(
                method="POST",
                path=world_path(self._region, "/reconstructions"),
                body=body,
                headers=headers,
            ),
        )
