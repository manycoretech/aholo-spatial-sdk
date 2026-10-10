from __future__ import annotations

from typing import TYPE_CHECKING

from manycore.aholo_sdk_core import AsyncAholoGatewayClient, assert_cmd_success

from .._paths import lux3d_path

if TYPE_CHECKING:
    from manycore.aholo_sdk_core import AholoGatewayClient


def _rig_body(model_url: str) -> dict:
    if not model_url or not model_url.strip():
        raise ValueError("model_url is required")
    return {"modelUrl": model_url}


class HumanoidAutoRigResource:
    def __init__(self, gateway: AholoGatewayClient, region: str) -> None:
        self._gateway = gateway
        self._region = region
    def create(self, *, model_url: str) -> int:
        response = self._gateway.gateway_request(method="POST",
            path=lux3d_path(self._region, "/animations/rig/task/create"),
            body=_rig_body(model_url))
        return int(assert_cmd_success(response, "humanoidAutoRig.create"))


class AsyncHumanoidAutoRigResource:
    def __init__(self, gateway: AsyncAholoGatewayClient, region: str) -> None:
        self._gateway = gateway
        self._region = region
    async def create(self, *, model_url: str) -> int:
        response = await self._gateway.gateway_request(method="POST",
            path=lux3d_path(self._region, "/animations/rig/task/create"),
            body=_rig_body(model_url))
        return int(assert_cmd_success(response, "humanoidAutoRig.create"))
