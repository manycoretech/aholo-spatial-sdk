from __future__ import annotations

import base64
import mimetypes
from pathlib import Path
from typing import TYPE_CHECKING, Optional

from manycore.aholo_sdk_core import assert_cmd_success

from .._paths import lux3d_path
from ..types import Lux3dVersion

if TYPE_CHECKING:
    from manycore.aholo_sdk_core import AholoGatewayClient


def _file_to_data_url(file_path: str | Path) -> str:
    path = Path(file_path)
    data = path.read_bytes()
    mime = mimetypes.guess_type(path.name)[0] or "application/octet-stream"
    return f"data:{mime};base64,{base64.b64encode(data).decode('ascii')}"


class ImgTo3dResource:
    def __init__(self, gateway: AholoGatewayClient, region: str) -> None:
        self._gateway = gateway
        self._region = region

    def create(self, *, img: str, version: Optional[Lux3dVersion] = None) -> int:
        """POST /generate/img-to-3d/task/create"""
        body: dict = {"img": img}
        if version is not None:
            body["version"] = version
        response = self._gateway.gateway_request(
            method="POST",
            path=lux3d_path(self._region, "/generate/img-to-3d/task/create"),
            body=body,
        )
        return int(assert_cmd_success(response, "imgTo3d.create"))

    def create_from_file(
        self,
        file_path: str | Path,
        *,
        version: Optional[Lux3dVersion] = None,
    ) -> int:
        """Create img-to-3D task from a local image file (encodes to Data URL automatically)."""
        return self.create(img=_file_to_data_url(file_path), version=version)
