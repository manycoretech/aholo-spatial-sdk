from __future__ import annotations

from typing import TYPE_CHECKING

from manycore.aholo_sdk_core import AsyncAholoGatewayClient, assert_cmd_success

from .._paths import lux3d_path

if TYPE_CHECKING:
    from manycore.aholo_sdk_core import AholoGatewayClient


def _part_split_body(glb_url: str) -> dict:
    if not glb_url or not glb_url.strip():
        raise ValueError("glb_url is required")
    return {"glbUrl": glb_url}


class PartSplitResource:
    def __init__(self, gateway: AholoGatewayClient, region: str) -> None:
        self._gateway = gateway
        self._region = region
    def create(self, *, glb_url: str) -> int:
        response = self._gateway.gateway_request(method="POST",
            path=lux3d_path(self._region, "/part-split/task/create"),
            body=_part_split_body(glb_url))
        return int(assert_cmd_success(response, "partSplit.create"))


class AsyncPartSplitResource:
    def __init__(self, gateway: AsyncAholoGatewayClient, region: str) -> None:
        self._gateway = gateway
        self._region = region
    async def create(self, *, glb_url: str) -> int:
        response = await self._gateway.gateway_request(method="POST",
            path=lux3d_path(self._region, "/part-split/task/create"),
            body=_part_split_body(glb_url))
        return int(assert_cmd_success(response, "partSplit.create"))
