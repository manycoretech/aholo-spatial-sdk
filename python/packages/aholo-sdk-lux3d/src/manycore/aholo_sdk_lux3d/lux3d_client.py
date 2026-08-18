from __future__ import annotations

from typing import Optional

from manycore.aholo_sdk_core import AholoClientConfig, create_gateway_client

from .resources.img_to_3d import ImgTo3dResource, _file_to_data_url
from .resources.material_transfer import MaterialTransferResource
from .resources.part_split import PartSplitResource
from .resources.tasks import TasksResource
from .resources.text_to_3d import TextTo3dResource

# Re-export for convenience: from manycore.aholo_sdk_lux3d import file_to_data_url
file_to_data_url = _file_to_data_url


class Lux3dClient:
    """
    Aholo Lux3D API client.

    Stainless-style resource access::

        lux3d = Lux3dClient(api_key="...")
        task_id = lux3d.img_to_3d.create(img="data:image/png;base64,...")
        task_id = lux3d.img_to_3d.create_from_file("/path/to/image.png")
        task_id = lux3d.text_to_3d.create(prompt="A wooden chair")
        task_id = lux3d.material_transfer.create(img="...", mesh_url="https://...")
        task_id = lux3d.part_split.create(glb_url="https://example.com/model.glb")
        result  = lux3d.tasks.retrieve(task_id)
        result  = lux3d.tasks.wait_for(task_id)
    """

    def __init__(self, config: Optional[AholoClientConfig] = None) -> None:
        cfg = config or AholoClientConfig()
        gateway = create_gateway_client(cfg)
        region: str = cfg.region or "cn"
        self.img_to_3d = ImgTo3dResource(gateway, region)
        self.text_to_3d = TextTo3dResource(gateway, region)
        self.material_transfer = MaterialTransferResource(gateway, region)
        self.part_split = PartSplitResource(gateway, region)
        self.tasks = TasksResource(gateway, region)


def create_lux3d_client(config: Optional[AholoClientConfig] = None) -> Lux3dClient:
    return Lux3dClient(config)
