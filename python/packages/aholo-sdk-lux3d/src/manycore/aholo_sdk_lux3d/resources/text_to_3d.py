from __future__ import annotations

from typing import TYPE_CHECKING, Optional

from manycore.aholo_sdk_core import assert_cmd_success

from .._paths import lux3d_path
from ..types import Lux3dStyle, Lux3dVersion

if TYPE_CHECKING:
    from manycore.aholo_sdk_core import AholoGatewayClient


class TextTo3dResource:
    def __init__(self, gateway: AholoGatewayClient, region: str) -> None:
        self._gateway = gateway
        self._region = region

    def create(
        self,
        *,
        prompt: str,
        style: Optional[Lux3dStyle] = None,
        img: Optional[str] = None,
        version: Optional[Lux3dVersion] = None,
    ) -> int:
        """POST /generate/text-to-3d/task/create"""
        body: dict = {"prompt": prompt}
        if style is not None:
            body["style"] = style
        if img is not None:
            body["img"] = img
        if version is not None:
            body["version"] = version
        response = self._gateway.gateway_request(
            method="POST",
            path=lux3d_path(self._region, "/generate/text-to-3d/task/create"),
            body=body,
        )
        return int(assert_cmd_success(response, "textTo3d.create"))

from manycore.aholo_sdk_core import AsyncAholoGatewayClient, assert_cmd_success

from .._paths import lux3d_path
from ..types import Lux3dStyle, Lux3dVersion


class AsyncTextTo3dResource:
    def __init__(self, gateway: AsyncAholoGatewayClient, region: str) -> None:
        self._gateway = gateway
        self._region = region

    async def create(
        self,
        *,
        prompt: str,
        style: Optional[Lux3dStyle] = None,
        img: Optional[str] = None,
        version: Optional[Lux3dVersion] = None,
    ) -> int:
        body: dict = {"prompt": prompt}
        if style is not None:
            body["style"] = style
        if img is not None:
            body["img"] = img
        if version is not None:
            body["version"] = version
        response = await self._gateway.gateway_request(
            method="POST",
            path=lux3d_path(self._region, "/generate/text-to-3d/task/create"),
            body=body,
        )
        return int(assert_cmd_success(response, "textTo3d.create"))
