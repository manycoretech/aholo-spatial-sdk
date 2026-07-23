from __future__ import annotations

from typing import TYPE_CHECKING, Optional, Sequence

from manycore.aholo_sdk_core import assert_cmd_success

from .._paths import lux3d_path
from ..resources.img_to_3d import _append_create_opts
from ..types import Lux3dOutputFormat, Lux3dVersion

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
        output_format: Optional[Sequence[Lux3dOutputFormat]] = None,
    ) -> int:
        """POST /generate/material-transfer/task/create. G1 is not supported."""
        body: dict = {"img": img, "meshUrl": mesh_url}
        _append_create_opts(
            body,
            version=version,
            output_format=output_format,
        )
        response = self._gateway.gateway_request(
            method="POST",
            path=lux3d_path(self._region, "/generate/material-transfer/task/create"),
            body=body,
        )
        return int(assert_cmd_success(response, "materialTransfer.create"))

from manycore.aholo_sdk_core import AsyncAholoGatewayClient, assert_cmd_success

from .._paths import lux3d_path
from ..types import Lux3dOutputFormat, Lux3dVersion


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
        output_format: Optional[Sequence[Lux3dOutputFormat]] = None,
    ) -> int:
        body: dict = {"img": img, "meshUrl": mesh_url}
        _append_create_opts(
            body,
            version=version,
            output_format=output_format,
        )
        response = await self._gateway.gateway_request(
            method="POST",
            path=lux3d_path(self._region, "/generate/material-transfer/task/create"),
            body=body,
        )
        return int(assert_cmd_success(response, "materialTransfer.create"))
