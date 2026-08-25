from __future__ import annotations

from typing import TYPE_CHECKING, Optional, Sequence

from manycore.aholo_sdk_core import AsyncAholoGatewayClient, assert_cmd_success

from .._paths import lux3d_path
from ..types import MaterialTransferOutputFormat, MaterialTransferVersion

if TYPE_CHECKING:
    from manycore.aholo_sdk_core import AholoGatewayClient


def _material_body(*, img: str, mesh_url: str, version: MaterialTransferVersion,
                   output_format: Optional[Sequence[MaterialTransferOutputFormat]] = None,
                   ai_predict_size: Optional[bool] = None,
                   custom_size: Optional[float] = None) -> dict:
    if version != "v3.0-standard":
        raise ValueError("version must be v3.0-standard")
    if custom_size is not None and custom_size <= 0:
        raise ValueError("custom_size must be greater than 0")
    body: dict = {"img": img, "meshUrl": mesh_url, "version": version}
    if output_format is not None:
        body["outputFormat"] = list(output_format)
    if ai_predict_size is not None:
        body["aiPredictSize"] = ai_predict_size
    if custom_size is not None:
        body["customSize"] = custom_size
    return body


class MaterialTransferResource:
    def __init__(self, gateway: AholoGatewayClient, region: str) -> None:
        self._gateway = gateway
        self._region = region
    def create(self, *, img: str, mesh_url: str, version: MaterialTransferVersion,
               output_format: Optional[Sequence[MaterialTransferOutputFormat]] = None,
               ai_predict_size: Optional[bool] = None, custom_size: Optional[float] = None) -> int:
        body = _material_body(img=img, mesh_url=mesh_url, version=version,
                              output_format=output_format, ai_predict_size=ai_predict_size,
                              custom_size=custom_size)
        response = self._gateway.gateway_request(method="POST",
            path=lux3d_path(self._region, "/generate/material-transfer/task/create"), body=body)
        return int(assert_cmd_success(response, "materialTransfer.create"))


class AsyncMaterialTransferResource:
    def __init__(self, gateway: AsyncAholoGatewayClient, region: str) -> None:
        self._gateway = gateway
        self._region = region
    async def create(self, *, img: str, mesh_url: str, version: MaterialTransferVersion,
                     output_format: Optional[Sequence[MaterialTransferOutputFormat]] = None,
                     ai_predict_size: Optional[bool] = None,
                     custom_size: Optional[float] = None) -> int:
        body = _material_body(img=img, mesh_url=mesh_url, version=version,
                              output_format=output_format, ai_predict_size=ai_predict_size,
                              custom_size=custom_size)
        response = await self._gateway.gateway_request(method="POST",
            path=lux3d_path(self._region, "/generate/material-transfer/task/create"), body=body)
        return int(assert_cmd_success(response, "materialTransfer.create"))
