from __future__ import annotations

from typing import Optional

from manycore.aholo_sdk_core import AholoClientConfig, create_async_gateway_client

from .resources.img_to_3d import AsyncImgTo3dResource
from .resources.material_transfer import AsyncMaterialTransferResource
from .resources.tasks import AsyncTasksResource
from .resources.text_to_3d import AsyncTextTo3dResource


class AsyncLux3dClient:
    """Async Aholo Lux3D API client."""

    def __init__(self, config: Optional[AholoClientConfig] = None) -> None:
        cfg = config or AholoClientConfig()
        gateway = create_async_gateway_client(cfg)
        region: str = cfg.region or "cn"
        self._gateway = gateway
        self.img_to_3d = AsyncImgTo3dResource(gateway, region)
        self.text_to_3d = AsyncTextTo3dResource(gateway, region)
        self.material_transfer = AsyncMaterialTransferResource(gateway, region)
        self.tasks = AsyncTasksResource(gateway, region)

    async def close(self) -> None:
        await self._gateway.close()

    async def __aenter__(self) -> "AsyncLux3dClient":
        return self

    async def __aexit__(self, *args: object) -> None:
        await self.close()


def create_async_lux3d_client(config: Optional[AholoClientConfig] = None) -> AsyncLux3dClient:
    return AsyncLux3dClient(config)
