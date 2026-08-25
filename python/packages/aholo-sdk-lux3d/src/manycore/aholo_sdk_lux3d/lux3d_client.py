from __future__ import annotations

from typing import Optional

from manycore.aholo_sdk_core import AholoClientConfig, create_gateway_client

from .resources.image_to_four_view import ImageToFourViewResource
from .resources.img_to_3d import ImgTo3dResource, _file_to_data_url
from .resources.material_transfer import MaterialTransferResource
from .resources.multi_format_export import MultiFormatExportResource
from .resources.tasks import TasksResource
from .resources.text_to_3d import TextTo3dResource

file_to_data_url = _file_to_data_url


class Lux3dClient:
    """Synchronous Aholo Lux3D API client."""
    def __init__(self, config: Optional[AholoClientConfig] = None) -> None:
        cfg = config or AholoClientConfig()
        gateway = create_gateway_client(cfg)
        region: str = cfg.region or "cn"
        self.image_to_four_view = ImageToFourViewResource(gateway, region)
        self.img_to_3d = ImgTo3dResource(gateway, region)
        self.text_to_3d = TextTo3dResource(gateway, region)
        self.material_transfer = MaterialTransferResource(gateway, region)
        self.multi_format_export = MultiFormatExportResource(gateway, region)
        self.tasks = TasksResource(gateway, region)


def create_lux3d_client(config: Optional[AholoClientConfig] = None) -> Lux3dClient:
    return Lux3dClient(config)
