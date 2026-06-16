from __future__ import annotations

from typing import TYPE_CHECKING, Optional

from manycore.aholo_sdk_core import assert_cmd_success

from .._paths import lux3d_path
from ..types import Lux3dVersion

if TYPE_CHECKING:
    from manycore.aholo_sdk_core import AholoGatewayClient


class MaterialTransferResource:
    def __init__(self, gateway: AholoGatewayClient, region: str) -> None:
        self._gateway = gateway
        self._region = region

    def create(
        self,
        *,
        img: str,
        mesh_url: str,
        version: Optional[Lux3dVersion] = None,
    ) -> int:
        """POST /generate/material-transfer/task/create"""
        body: dict = {"img": img, "meshUrl": mesh_url}
        if version is not None:
            body["version"] = version
        response = self._gateway.gateway_request(
            method="POST",
            path=lux3d_path(self._region, "/generate/material-transfer/task/create"),
            body=body,
        )
        return int(assert_cmd_success(response, "materialTransfer.create"))

from manycore.aholo_sdk_core import AsyncAholoGatewayClient, assert_cmd_success

from .._paths import lux3d_path
from ..types import Lux3dVersion


class AsyncMaterialTransferResource:
    def __init__(self, gateway: AsyncAholoGatewayClient, region: str) -> None:
        self._gateway = gateway
        self._region = region

    async def create(
        self,
        *,
        img: str,
        mesh_url: str,
        version: Optional[Lux3dVersion] = None,
    ) -> int:
        body: dict = {"img": img, "meshUrl": mesh_url}
        if version is not None:
            body["version"] = version
        response = await self._gateway.gateway_request(
            method="POST",
            path=lux3d_path(self._region, "/generate/material-transfer/task/create"),
            body=body,
        )
        return int(assert_cmd_success(response, "materialTransfer.create"))
