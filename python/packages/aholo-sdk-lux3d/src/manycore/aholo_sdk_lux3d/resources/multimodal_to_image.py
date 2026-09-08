from __future__ import annotations

import asyncio
from pathlib import Path
from typing import TYPE_CHECKING, Optional

from manycore.aholo_sdk_core import AsyncAholoGatewayClient, assert_cmd_success

from .._paths import lux3d_path
from .img_to_3d import _file_to_data_url

if TYPE_CHECKING:
    from manycore.aholo_sdk_core import AholoGatewayClient


def _img_or_prompt_body(*, img: Optional[str] = None, prompt: Optional[str] = None) -> dict:
    if img is not None and not str(img).strip():
        raise ValueError("img must not be empty")
    if prompt is not None and not str(prompt).strip():
        raise ValueError("prompt must not be empty")
    if img is None and prompt is None:
        raise ValueError("img or prompt is required")
    body: dict = {}
    if img is not None:
        body["img"] = img
    if prompt is not None:
        body["prompt"] = prompt
    return body


class MultimodalToImageResource:
    def __init__(self, gateway: AholoGatewayClient, region: str) -> None:
        self._gateway = gateway
        self._region = region

    def create(self, *, img: Optional[str] = None, prompt: Optional[str] = None) -> int:
        response = self._gateway.gateway_request(
            method="POST",
            path=lux3d_path(self._region, "/generate/multimodal-to-image/task/create"),
            body=_img_or_prompt_body(img=img, prompt=prompt),
        )
        return int(assert_cmd_success(response, "multimodalToImage.create"))

    def create_from_file(self, file_path: str | Path | None = None, *, prompt: Optional[str] = None) -> int:
        img = _file_to_data_url(file_path) if file_path is not None else None
        return self.create(img=img, prompt=prompt)


class AsyncMultimodalToImageResource:
    def __init__(self, gateway: AsyncAholoGatewayClient, region: str) -> None:
        self._gateway = gateway
        self._region = region

    async def create(self, *, img: Optional[str] = None, prompt: Optional[str] = None) -> int:
        response = await self._gateway.gateway_request(
            method="POST",
            path=lux3d_path(self._region, "/generate/multimodal-to-image/task/create"),
            body=_img_or_prompt_body(img=img, prompt=prompt),
        )
        return int(assert_cmd_success(response, "multimodalToImage.create"))

    async def create_from_file(self, file_path: str | Path | None = None, *, prompt: Optional[str] = None) -> int:
        img = await asyncio.to_thread(_file_to_data_url, file_path) if file_path is not None else None
        return await self.create(img=img, prompt=prompt)
