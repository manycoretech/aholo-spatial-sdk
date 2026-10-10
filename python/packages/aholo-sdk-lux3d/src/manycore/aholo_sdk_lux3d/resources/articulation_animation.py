from __future__ import annotations

from typing import TYPE_CHECKING

from manycore.aholo_sdk_core import AsyncAholoGatewayClient, assert_cmd_success

from .._paths import lux3d_path

if TYPE_CHECKING:
    from manycore.aholo_sdk_core import AholoGatewayClient


def _articulation_body(*, glb_url: str, prompt: str) -> dict:
    if not glb_url or not glb_url.strip() or not prompt or not prompt.strip():
        raise ValueError("glb_url and prompt are required")
    return {"glbUrl": glb_url, "prompt": prompt}


class ArticulationAnimationResource:
    def __init__(self, gateway: AholoGatewayClient, region: str) -> None:
        self._gateway = gateway
        self._region = region
    def create(self, *, glb_url: str, prompt: str) -> int:
        response = self._gateway.gateway_request(method="POST",
            path=lux3d_path(self._region, "/articulation-animation/task/create"),
            body=_articulation_body(glb_url=glb_url, prompt=prompt))
        return int(assert_cmd_success(response, "articulationAnimation.create"))


class AsyncArticulationAnimationResource:
    def __init__(self, gateway: AsyncAholoGatewayClient, region: str) -> None:
        self._gateway = gateway
        self._region = region
    async def create(self, *, glb_url: str, prompt: str) -> int:
        response = await self._gateway.gateway_request(method="POST",
            path=lux3d_path(self._region, "/articulation-animation/task/create"),
            body=_articulation_body(glb_url=glb_url, prompt=prompt))
        return int(assert_cmd_success(response, "articulationAnimation.create"))
