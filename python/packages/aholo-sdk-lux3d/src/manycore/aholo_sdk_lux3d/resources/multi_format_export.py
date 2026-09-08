from __future__ import annotations

from typing import TYPE_CHECKING, Optional, Sequence
from urllib.parse import urlsplit

from manycore.aholo_sdk_core import AsyncAholoGatewayClient, assert_cmd_success

from .._paths import lux3d_path
from ..types import MultiFormatExportOutputFormat

if TYPE_CHECKING:
    from manycore.aholo_sdk_core import AholoGatewayClient


_OUTPUT_FORMATS = frozenset({"usdz", "obj_zip", "fbx_zip", "stl", "3mf"})


def _export_body(*, model_url: str,
                 output_format: Optional[Sequence[MultiFormatExportOutputFormat]] = None) -> dict:
    if output_format is not None and any(fmt not in _OUTPUT_FORMATS for fmt in output_format):
        raise ValueError("output_format supports usdz, obj_zip, fbx_zip, stl, and 3mf")
    if urlsplit(model_url).path.lower().endswith(".glb") and not output_format:
        raise ValueError("output_format is required for GLB input")
    body: dict = {"modelUrl": model_url}
    if output_format is not None:
        body["outputFormat"] = list(output_format)
    return body


class MultiFormatExportResource:
    def __init__(self, gateway: AholoGatewayClient, region: str) -> None:
        self._gateway = gateway
        self._region = region
    def create(self, *, model_url: str,
               output_format: Optional[Sequence[MultiFormatExportOutputFormat]] = None) -> int:
        response = self._gateway.gateway_request(method="POST",
            path=lux3d_path(self._region, "/multi-format-export/task/create"),
            body=_export_body(model_url=model_url, output_format=output_format))
        return int(assert_cmd_success(response, "multiFormatExport.create"))


class AsyncMultiFormatExportResource:
    def __init__(self, gateway: AsyncAholoGatewayClient, region: str) -> None:
        self._gateway = gateway
        self._region = region
    async def create(self, *, model_url: str,
                     output_format: Optional[Sequence[MultiFormatExportOutputFormat]] = None) -> int:
        response = await self._gateway.gateway_request(method="POST",
            path=lux3d_path(self._region, "/multi-format-export/task/create"),
            body=_export_body(model_url=model_url, output_format=output_format))
        return int(assert_cmd_success(response, "multiFormatExport.create"))
