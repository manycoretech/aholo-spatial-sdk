from __future__ import annotations

from typing import TYPE_CHECKING

from manycore.aholo_sdk_core import AsyncAholoGatewayClient, assert_cmd_success

from .._paths import lux3d_path

if TYPE_CHECKING:
    from manycore.aholo_sdk_core import AholoGatewayClient


class ImageToFourViewResource:
    def __init__(self, gateway: AholoGatewayClient, region: str) -> None:
        self._gateway = gateway
        self._region = region
    def create(self, *, img: str) -> int:
        response = self._gateway.gateway_request(method="POST",
            path=lux3d_path(self._region, "/generate/image-to-four-view/task/create"),
            body={"img": img})
        return int(assert_cmd_success(response, "imageToFourView.create"))


class AsyncImageToFourViewResource:
    def __init__(self, gateway: AsyncAholoGatewayClient, region: str) -> None:
        self._gateway = gateway
        self._region = region
    async def create(self, *, img: str) -> int:
        response = await self._gateway.gateway_request(method="POST",
            path=lux3d_path(self._region, "/generate/image-to-four-view/task/create"),
            body={"img": img})
        return int(assert_cmd_success(response, "imageToFourView.create"))
