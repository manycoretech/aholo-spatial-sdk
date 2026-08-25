from __future__ import annotations

from typing import TYPE_CHECKING, Optional, Sequence

from manycore.aholo_sdk_core import AsyncAholoGatewayClient, assert_cmd_success

from .._paths import lux3d_path
from ..resources.img_to_3d import _append_create_opts
from ..types import Lux3dOutputFormat, Lux3dStyle, Lux3dVersion

if TYPE_CHECKING:
    from manycore.aholo_sdk_core import AholoGatewayClient


def _text_body(*, prompt: str, version: Lux3dVersion, style: Optional[Lux3dStyle] = None,
               img: Optional[str] = None, face_count: Optional[int] = None,
               output_format: Optional[Sequence[Lux3dOutputFormat]] = None,
               enable_pbr: Optional[bool] = None, ai_predict_size: Optional[bool] = None) -> dict:
    if not prompt.strip():
        raise ValueError("prompt must not be empty")
    body: dict = {"prompt": prompt}
    if style is not None:
        body["style"] = style
    if img is not None:
        body["img"] = img
    _append_create_opts(body, version=version, face_count=face_count, output_format=output_format,
                        enable_pbr=enable_pbr, ai_predict_size=ai_predict_size)
    return body


class TextTo3dResource:
    def __init__(self, gateway: AholoGatewayClient, region: str) -> None:
        self._gateway = gateway
        self._region = region

    def create(self, *, prompt: str, version: Lux3dVersion, style: Optional[Lux3dStyle] = None,
               img: Optional[str] = None, face_count: Optional[int] = None,
               output_format: Optional[Sequence[Lux3dOutputFormat]] = None,
               enable_pbr: Optional[bool] = None, ai_predict_size: Optional[bool] = None) -> int:
        body = _text_body(prompt=prompt, version=version, style=style, img=img, face_count=face_count,
                          output_format=output_format, enable_pbr=enable_pbr,
                          ai_predict_size=ai_predict_size)
        response = self._gateway.gateway_request(method="POST",
            path=lux3d_path(self._region, "/generate/text-to-3d/task/create"), body=body)
        return int(assert_cmd_success(response, "textTo3d.create"))


class AsyncTextTo3dResource:
    def __init__(self, gateway: AsyncAholoGatewayClient, region: str) -> None:
        self._gateway = gateway
        self._region = region

    async def create(self, *, prompt: str, version: Lux3dVersion,
                     style: Optional[Lux3dStyle] = None, img: Optional[str] = None,
                     face_count: Optional[int] = None,
                     output_format: Optional[Sequence[Lux3dOutputFormat]] = None,
                     enable_pbr: Optional[bool] = None,
                     ai_predict_size: Optional[bool] = None) -> int:
        body = _text_body(prompt=prompt, version=version, style=style, img=img, face_count=face_count,
                          output_format=output_format, enable_pbr=enable_pbr,
                          ai_predict_size=ai_predict_size)
        response = await self._gateway.gateway_request(method="POST",
            path=lux3d_path(self._region, "/generate/text-to-3d/task/create"), body=body)
        return int(assert_cmd_success(response, "textTo3d.create"))
