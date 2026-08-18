from __future__ import annotations

from typing import TYPE_CHECKING

from manycore.aholo_sdk_core import assert_cmd_success

from .._paths import lux3d_path

if TYPE_CHECKING:
    from manycore.aholo_sdk_core import AholoGatewayClient


class PartSplitResource:
    def __init__(self, gateway: AholoGatewayClient, region: str) -> None:
        self._gateway = gateway
        self._region = region

    def create(self, *, glb_url: str) -> int:
        """POST /part-split/task/create."""
        if not glb_url:
            raise ValueError("glb_url must not be empty")
        response = self._gateway.gateway_request(
            method="POST",
            path=lux3d_path(self._region, "/part-split/task/create"),
            body={"glbUrl": glb_url},
        )
        return int(assert_cmd_success(response, "partSplit.create"))


from manycore.aholo_sdk_core import AsyncAholoGatewayClient


class AsyncPartSplitResource:
    def __init__(self, gateway: AsyncAholoGatewayClient, region: str) -> None:
        self._gateway = gateway
        self._region = region

    async def create(self, *, glb_url: str) -> int:
        if not glb_url:
            raise ValueError("glb_url must not be empty")
        response = await self._gateway.gateway_request(
            method="POST",
            path=lux3d_path(self._region, "/part-split/task/create"),
            body={"glbUrl": glb_url},
        )
        return int(assert_cmd_success(response, "partSplit.create"))
