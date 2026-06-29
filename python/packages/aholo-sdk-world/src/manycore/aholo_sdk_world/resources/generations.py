from __future__ import annotations

from typing import TYPE_CHECKING, List, Optional, cast

from .._paths import world_path
from ..types import GenerateWorldResourceItem, WorldAsyncOperation

if TYPE_CHECKING:
    from manycore.aholo_sdk_core import AholoGatewayClient


class GenerationsResource:
    def __init__(self, gateway: AholoGatewayClient, region: str) -> None:
        self._gateway = gateway
        self._region = region

    def create(
        self,
        *,
        prompt: Optional[str] = None,
        resources: Optional[List[GenerateWorldResourceItem]] = None,
        name: Optional[str] = None,
        cover: Optional[str] = None,
        x_source: Optional[str] = None,
    ) -> WorldAsyncOperation:
        """POST /world/v1/generations — Create a 3DGS generation job."""
        body: dict = {}
        if prompt is not None:
            body["prompt"] = prompt
        if resources is not None:
            body["resources"] = resources
        if name is not None:
            body["name"] = name
        if cover is not None:
            body["cover"] = cover
        headers = {"x-source": x_source} if x_source else None
        return cast(
            WorldAsyncOperation,
            self._gateway.gateway_request(
                method="POST",
                path=world_path(self._region, "/generations"),
                body=body,
                headers=headers,
            ),
        )

from manycore.aholo_sdk_core import AsyncAholoGatewayClient


class AsyncGenerationsResource:
    def __init__(self, gateway: AsyncAholoGatewayClient, region: str) -> None:
        self._gateway = gateway
        self._region = region

    async def create(
        self,
        *,
        prompt: Optional[str] = None,
        resources: Optional[List[GenerateWorldResourceItem]] = None,
        name: Optional[str] = None,
        cover: Optional[str] = None,
        x_source: Optional[str] = None,
    ) -> WorldAsyncOperation:
        """POST /world/v1/generations — Create a 3DGS generation job."""
        body: dict = {}
        if prompt is not None:
            body["prompt"] = prompt
        if resources is not None:
            body["resources"] = resources
        if name is not None:
            body["name"] = name
        if cover is not None:
            body["cover"] = cover
        headers = {"x-source": x_source} if x_source else None
        return cast(
            WorldAsyncOperation,
            await self._gateway.gateway_request(
                method="POST",
                path=world_path(self._region, "/generations"),
                body=body,
                headers=headers,
            ),
        )
