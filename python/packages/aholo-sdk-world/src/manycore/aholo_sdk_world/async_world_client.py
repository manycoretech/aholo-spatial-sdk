from __future__ import annotations

from typing import Optional, cast
from urllib.parse import quote

from manycore.aholo_sdk_core import AholoClientConfig, create_async_gateway_client, poll_until_async

from ._paths import world_path
from .resources.generations import AsyncGenerationsResource
from .resources.reconstructions import AsyncReconstructionsResource
from .types import WORLD_TERMINAL_FAILURE_STATUSES, WorldDetail, WorldPagedList

DEFAULT_POLL_INTERVAL_MS = 5_000
DEFAULT_POLL_TIMEOUT_MS = 86_400_000  # 24 hours


class AsyncWorldClient:
    """
    Async Aholo 3DGS world API client.

    Stainless-style resource access::

        async with AsyncWorldClient(api_key="...") as world:
            op = await world.reconstructions.create(resources=[...], task_quality="normal", scene="space")
            result = await world.wait_for(op["worldId"])
    """

    def __init__(self, config: Optional[AholoClientConfig] = None) -> None:
        cfg = config or AholoClientConfig()
        self._gateway = create_async_gateway_client(cfg)
        self._region: str = cfg.region or "cn"
        self.reconstructions = AsyncReconstructionsResource(self._gateway, self._region)
        self.generations = AsyncGenerationsResource(self._gateway, self._region)

    async def close(self) -> None:
        await self._gateway.close()

    async def __aenter__(self) -> "AsyncWorldClient":
        return self

    async def __aexit__(self, *args: object) -> None:
        await self.close()

    async def retrieve(self, world_id: str, *, x_source: Optional[str] = None) -> WorldDetail:
        """GET /world/v1/{worldId}"""
        headers = {"x-source": x_source} if x_source else None
        return cast(
            WorldDetail,
            await self._gateway.gateway_request(
                method="GET",
                path=world_path(self._region, f"/{quote(world_id, safe='')}"),
                headers=headers,
            ),
        )

    async def list(
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
            await self._gateway.gateway_request(
                method="POST",
                path=world_path(self._region, "/list"),
                body=body,
                headers=headers,
            ),
        )

    async def wait_for(
        self,
        world_id: str,
        *,
        interval_ms: int = DEFAULT_POLL_INTERVAL_MS,
        timeout_ms: int = DEFAULT_POLL_TIMEOUT_MS,
    ) -> WorldDetail:
        """Poll world detail until SUCCEEDED or a terminal failure status."""
        return await poll_until_async(
            lambda: self.retrieve(world_id),
            is_done=lambda d: d.get("status") == "SUCCEEDED",
            is_failed=lambda d: d.get("status") in WORLD_TERMINAL_FAILURE_STATUSES,
            fail_message=lambda d: f"World failed worldId={world_id} status={d.get('status')}",
            interval_ms=interval_ms,
            timeout_ms=timeout_ms,
        )


def create_async_world_client(config: Optional[AholoClientConfig] = None) -> AsyncWorldClient:
    return AsyncWorldClient(config)
