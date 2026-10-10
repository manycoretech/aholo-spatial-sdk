from __future__ import annotations

from typing import TYPE_CHECKING, Optional, Sequence

from manycore.aholo_sdk_core import AsyncAholoGatewayClient, assert_cmd_success

from .._paths import lux3d_path
from ..humanoid_animations import HUMANOID_ANIMATION_IDS

if TYPE_CHECKING:
    from manycore.aholo_sdk_core import AholoGatewayClient

_OUTPUT_MODES = frozenset({"separate", "combined"})
_OUT_FORMATS = frozenset({"glb", "fbx"})


def _retarget_body(*, rig_model_url: str, animation_ids: Sequence[str],
                   animation_output_mode: Optional[str] = None, out_format: Optional[str] = None,
                   animate_in_place: Optional[bool] = None) -> dict:
    if not rig_model_url or not rig_model_url.strip():
        raise ValueError("rig_model_url is required")
    ids = list(animation_ids)
    if (not 1 <= len(ids) <= 10 or len(set(ids)) != len(ids)
            or any(item not in HUMANOID_ANIMATION_IDS for item in ids)):
        raise ValueError("animation_ids must be 1 to 10 unique humanoid animation ids")
    if animation_output_mode is not None and animation_output_mode not in _OUTPUT_MODES:
        raise ValueError("animation_output_mode must be separate or combined")
    if out_format is not None and out_format not in _OUT_FORMATS:
        raise ValueError("out_format must be glb or fbx")
    body: dict = {"rigModelUrl": rig_model_url, "animationIds": ids}
    if animation_output_mode is not None:
        body["animationOutputMode"] = animation_output_mode
    if out_format is not None:
        body["outFormat"] = out_format
    if animate_in_place is not None:
        body["animateInPlace"] = animate_in_place
    return body


class HumanoidAnimationRetargetResource:
    def __init__(self, gateway: AholoGatewayClient, region: str) -> None:
        self._gateway = gateway
        self._region = region
    def create(self, *, rig_model_url: str, animation_ids: Sequence[str],
               animation_output_mode: Optional[str] = None, out_format: Optional[str] = None,
               animate_in_place: Optional[bool] = None) -> int:
        response = self._gateway.gateway_request(method="POST",
            path=lux3d_path(self._region, "/animations/retarget/task/create"),
            body=_retarget_body(rig_model_url=rig_model_url, animation_ids=animation_ids,
                                animation_output_mode=animation_output_mode, out_format=out_format,
                                animate_in_place=animate_in_place))
        return int(assert_cmd_success(response, "humanoidAnimationRetarget.create"))


class AsyncHumanoidAnimationRetargetResource:
    def __init__(self, gateway: AsyncAholoGatewayClient, region: str) -> None:
        self._gateway = gateway
        self._region = region
    async def create(self, *, rig_model_url: str, animation_ids: Sequence[str],
                     animation_output_mode: Optional[str] = None, out_format: Optional[str] = None,
                     animate_in_place: Optional[bool] = None) -> int:
        response = await self._gateway.gateway_request(method="POST",
            path=lux3d_path(self._region, "/animations/retarget/task/create"),
            body=_retarget_body(rig_model_url=rig_model_url, animation_ids=animation_ids,
                                animation_output_mode=animation_output_mode, out_format=out_format,
                                animate_in_place=animate_in_place))
        return int(assert_cmd_success(response, "humanoidAnimationRetarget.create"))
