from __future__ import annotations

from typing import Optional, cast
from urllib.parse import quote

from manycore.aholo_sdk_core import AholoClientConfig, create_gateway_client, poll_until

from ._paths import world_path
from .resources.generations import GenerationsResource
from .resources.reconstructions import ReconstructionsResource
from .types import WORLD_TERMINAL_FAILURE_STATUSES, WorldDetail, WorldPagedList

DEFAULT_POLL_INTERVAL_MS = 5_000
DEFAULT_POLL_TIMEOUT_MS = 86_400_000  # 24 hours


class WorldClient:
    """
    Aholo 3DGS world API client.

    Stainless-style resource access::

        world  = WorldClient(api_key="...")
        op     = world.reconstructions.create(resources=[...], task_quality="normal", scene="space")
        op     = world.generations.create(prompt="Modern living room")
        detail = world.retrieve(op["worldId"])
        result = world.wait_for(op["worldId"])
        page   = world.list(page_num=0, page_size=20)
    """

    def __init__(self, config: Optional[AholoClientConfig] = None) -> None:
        cfg = config or AholoClientConfig()
        self._gateway = create_gateway_client(cfg)
        self._region: str = cfg.region or "cn"
        self.reconstructions = ReconstructionsResource(self._gateway, self._region)
        self.generations = GenerationsResource(self._gateway, self._region)

    def retrieve(self, world_id: str, *, x_source: Optional[str] = None) -> WorldDetail:
        """GET /world/v1/{worldId}"""
        headers = {"x-source": x_source} if x_source else None
        return cast(
            WorldDetail,
            self._gateway.gateway_request(
                method="GET",
                path=world_path(self._region, f"/{quote(world_id, safe='')}"),
                headers=headers,
            ),
        )

    def list(
        self,
        *,
        page_num: Optional[int] = None,
        page_size: Optional[int] = None,
        status_list: Optional[list] = None,
        x_source: Optional[str] = None,
    ) -> WorldPagedList:
        """POST /world/v1/list"""
        body: dict = {}
        if page_num is not None:
            body["pageNum"] = page_num
        if page_size is not None:
            body["pageSize"] = page_size
        if status_list is not None:
            body["statusList"] = status_list
        headers = {"x-source": x_source} if x_source else None
        return cast(
            WorldPagedList,
            self._gateway.gateway_request(
                method="POST",
                path=world_path(self._region, "/list"),
                body=body,
                headers=headers,
            ),
        )

    def wait_for(
        self,
        world_id: str,
        *,
        interval_ms: int = DEFAULT_POLL_INTERVAL_MS,
        timeout_ms: int = DEFAULT_POLL_TIMEOUT_MS,
    ) -> WorldDetail:
        """Poll world detail until SUCCEEDED or a terminal failure status."""
        return poll_until(
            lambda: self.retrieve(world_id),
            is_done=lambda d: d.get("status") == "SUCCEEDED",
            is_failed=lambda d: d.get("status") in WORLD_TERMINAL_FAILURE_STATUSES,
            fail_message=lambda d: f"World failed worldId={world_id} status={d.get('status')}",
            interval_ms=interval_ms,
            timeout_ms=timeout_ms,
        )


def create_world_client(config: Optional[AholoClientConfig] = None) -> WorldClient:
    return WorldClient(config)
