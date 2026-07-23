from __future__ import annotations

import base64
import mimetypes
from pathlib import Path
from typing import TYPE_CHECKING, Optional, Sequence

from manycore.aholo_sdk_core import assert_cmd_success

from .._paths import lux3d_path
from ..types import Lux3dOutputFormat, Lux3dVersion

if TYPE_CHECKING:
    from manycore.aholo_sdk_core import AholoGatewayClient


def _file_to_data_url(file_path: str | Path) -> str:
    path = Path(file_path)
    data = path.read_bytes()
    mime = mimetypes.guess_type(path.name)[0] or "application/octet-stream"
    return f"data:{mime};base64,{base64.b64encode(data).decode('ascii')}"


def _append_create_opts(
    body: dict,
    *,
    version: Optional[Lux3dVersion] = None,
    face_count: Optional[int] = None,
    output_format: Optional[Sequence[Lux3dOutputFormat]] = None,
    enable_pbr: Optional[bool] = None,
    texture_size: Optional[int] = None,
) -> None:
    if version is not None:
        body["version"] = version
    if face_count is not None:
        body["faceCount"] = face_count
    if output_format is not None:
        body["outputFormat"] = list(output_format)
    if enable_pbr is not None:
        body["enablePbr"] = enable_pbr
    if texture_size is not None:
        body["textureSize"] = texture_size


def _img_body(
    *,
    img: Optional[str] = None,
    imgs: Optional[Sequence[str]] = None,
) -> dict:
    has_img = img is not None
    has_imgs = imgs is not None
    if has_img == has_imgs:
        raise ValueError("Provide exactly one of img or imgs")
    if has_imgs and len(imgs) == 0:
        raise ValueError("imgs must not be empty")
    return {"img": img} if has_img else {"imgs": list(imgs)}


class ImgTo3dResource:
    def __init__(self, gateway: AholoGatewayClient, region: str) -> None:
        self._gateway = gateway
        self._region = region

    def create(
        self,
        *,
        img: Optional[str] = None,
        imgs: Optional[Sequence[str]] = None,
        version: Optional[Lux3dVersion] = None,
        face_count: Optional[int] = None,
        output_format: Optional[Sequence[Lux3dOutputFormat]] = None,
        enable_pbr: Optional[bool] = None,
        texture_size: Optional[int] = None,
    ) -> int:
        """POST /generate/img-to-3d/task/create"""
        body = _img_body(img=img, imgs=imgs)
        _append_create_opts(
            body,
            version=version,
            face_count=face_count,
            output_format=output_format,
            enable_pbr=enable_pbr,
            texture_size=texture_size,
        )
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
        face_count: Optional[int] = None,
        output_format: Optional[Sequence[Lux3dOutputFormat]] = None,
        enable_pbr: Optional[bool] = None,
        texture_size: Optional[int] = None,
    ) -> int:
        """Create img-to-3D task from a local image file (encodes to Data URL automatically)."""
        return self.create(
            img=_file_to_data_url(file_path),
            version=version,
            face_count=face_count,
            output_format=output_format,
            enable_pbr=enable_pbr,
            texture_size=texture_size,
        )

    def create_from_files(
        self,
        file_paths: Sequence[str | Path],
        *,
        version: Optional[Lux3dVersion] = None,
        face_count: Optional[int] = None,
        output_format: Optional[Sequence[Lux3dOutputFormat]] = None,
        enable_pbr: Optional[bool] = None,
        texture_size: Optional[int] = None,
    ) -> int:
        """Create a G1 multi-view img-to-3D task from local image files (sent as ``imgs``)."""
        return self.create(
            imgs=[_file_to_data_url(p) for p in file_paths],
            version=version,
            face_count=face_count,
            output_format=output_format,
            enable_pbr=enable_pbr,
            texture_size=texture_size,
        )

import asyncio

from manycore.aholo_sdk_core import AsyncAholoGatewayClient, assert_cmd_success

from .._paths import lux3d_path
from ..types import Lux3dOutputFormat, Lux3dVersion


class AsyncImgTo3dResource:
    def __init__(self, gateway: AsyncAholoGatewayClient, region: str) -> None:
        self._gateway = gateway
        self._region = region

    async def create(
        self,
        *,
        img: Optional[str] = None,
        imgs: Optional[Sequence[str]] = None,
        version: Optional[Lux3dVersion] = None,
        face_count: Optional[int] = None,
        output_format: Optional[Sequence[Lux3dOutputFormat]] = None,
        enable_pbr: Optional[bool] = None,
        texture_size: Optional[int] = None,
    ) -> int:
        body = _img_body(img=img, imgs=imgs)
        _append_create_opts(
            body,
            version=version,
            face_count=face_count,
            output_format=output_format,
            enable_pbr=enable_pbr,
            texture_size=texture_size,
        )
        response = await self._gateway.gateway_request(
            method="POST",
            path=lux3d_path(self._region, "/generate/img-to-3d/task/create"),
            body=body,
        )
        return int(assert_cmd_success(response, "imgTo3d.create"))

    async def create_from_file(
        self,
        file_path: str | Path,
        *,
        version: Optional[Lux3dVersion] = None,
        face_count: Optional[int] = None,
        output_format: Optional[Sequence[Lux3dOutputFormat]] = None,
        enable_pbr: Optional[bool] = None,
        texture_size: Optional[int] = None,
    ) -> int:
        img = await asyncio.to_thread(_file_to_data_url, file_path)
        return await self.create(
            img=img,
            version=version,
            face_count=face_count,
            output_format=output_format,
            enable_pbr=enable_pbr,
            texture_size=texture_size,
        )

    async def create_from_files(
        self,
        file_paths: Sequence[str | Path],
        *,
        version: Optional[Lux3dVersion] = None,
        face_count: Optional[int] = None,
        output_format: Optional[Sequence[Lux3dOutputFormat]] = None,
        enable_pbr: Optional[bool] = None,
        texture_size: Optional[int] = None,
    ) -> int:
        imgs = await asyncio.gather(
            *[asyncio.to_thread(_file_to_data_url, p) for p in file_paths]
        )
        return await self.create(
            imgs=list(imgs),
            version=version,
            face_count=face_count,
            output_format=output_format,
            enable_pbr=enable_pbr,
            texture_size=texture_size,
        )
