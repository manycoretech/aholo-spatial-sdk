import asyncio
import unittest

from manycore.aholo_sdk_lux3d.resources.image_to_four_view import (
    AsyncImageToFourViewResource, ImageToFourViewResource,
)
from manycore.aholo_sdk_lux3d.resources.material_transfer import MaterialTransferResource
from manycore.aholo_sdk_lux3d.resources.multi_format_export import (
    AsyncMultiFormatExportResource, MultiFormatExportResource,
)
from manycore.aholo_sdk_lux3d.resources.tasks import AsyncTasksResource, TasksResource


class FakeGateway:
    def __init__(self):
        self.requests = []

    def gateway_request(self, **kwargs):
        self.requests.append(kwargs)
        if kwargs["method"] == "POST":
            return {"c": "0", "m": "", "d": 42, "f": None}
        if kwargs["path"].endswith("/task/get"):
            return {"c": "0", "m": "", "d": {
                "taskId": 42, "bizId": "LUX_3D", "status": 6, "outputs": []}}
        return {"c": "0", "m": "", "d": {
            "items": [], "total": 0, "page": 2, "pageSize": 10}}


class AsyncFakeGateway(FakeGateway):
    async def gateway_request(self, **kwargs):
        return super().gateway_request(**kwargs)


class NewApisTest(unittest.TestCase):
    def test_image_to_four_view(self):
        gateway = FakeGateway()
        task_id = ImageToFourViewResource(gateway, "cn").create(
            img="https://example.com/image.png")
        self.assertEqual(42, task_id)
        self.assertEqual("/lux3d/v1/generate/image-to-four-view/task/create",
                         gateway.requests[0]["path"])

    def test_multi_format_export(self):
        gateway = FakeGateway()
        task_id = MultiFormatExportResource(gateway, "com").create(
            model_url="https://example.com/model.glb", output_format=["usdz"])
        self.assertEqual(42, task_id)
        self.assertEqual("/global/lux3d/v1/multi-format-export/task/create",
                         gateway.requests[0]["path"])
        with self.assertRaises(ValueError):
            MultiFormatExportResource(gateway, "cn").create(
                model_url="https://example.com/model.glb")

    def test_material_transfer_new_fields(self):
        gateway = FakeGateway()
        MaterialTransferResource(gateway, "cn").create(
            img="https://example.com/material.png",
            mesh_url="https://example.com/model.glb",
            version="v3.0-standard",
            ai_predict_size=False,
            custom_size=120.5,
        )
        self.assertEqual(False, gateway.requests[0]["body"]["aiPredictSize"])
        self.assertEqual(120.5, gateway.requests[0]["body"]["customSize"])

    def test_tasks_include_biz_id_and_accept_canceled(self):
        gateway = FakeGateway()
        tasks = TasksResource(gateway, "com")
        result = tasks.retrieve(42)
        self.assertEqual("LUX_3D", result["bizId"])
        page = tasks.list(status=6)
        self.assertEqual(0, page["total"])
        self.assertEqual(6, gateway.requests[1]["query"]["status"])

    def test_async_resources(self):
        async def run():
            gateway = AsyncFakeGateway()
            four_view_id = await AsyncImageToFourViewResource(gateway, "cn").create(
                img="https://example.com/image.png")
            export_id = await AsyncMultiFormatExportResource(gateway, "cn").create(
                model_url="https://example.com/model.glb", output_format=["obj_zip"])
            page = await AsyncTasksResource(gateway, "cn").list(status=6)
            self.assertEqual((42, 42, 0), (four_view_id, export_id, page["total"]))

        asyncio.run(run())


if __name__ == "__main__":
    unittest.main()
