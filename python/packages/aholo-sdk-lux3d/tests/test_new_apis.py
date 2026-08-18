import asyncio
import unittest

from manycore.aholo_sdk_lux3d.resources.part_split import AsyncPartSplitResource, PartSplitResource
from manycore.aholo_sdk_lux3d.resources.tasks import AsyncTasksResource, TasksResource


class FakeGateway:
    def __init__(self):
        self.requests = []

    def gateway_request(self, **kwargs):
        self.requests.append(kwargs)
        if kwargs["path"].endswith("/part-split/task/create"):
            return {"c": "0", "m": "", "d": 42}
        return {"c": "0", "m": "", "d": {"items": [], "total": 0, "page": 2, "pageSize": 10}}


class AsyncFakeGateway(FakeGateway):
    async def gateway_request(self, **kwargs):
        return super().gateway_request(**kwargs)


class NewApisTest(unittest.TestCase):
    def test_part_split(self):
        gateway = FakeGateway()
        task_id = PartSplitResource(gateway, "cn").create(glb_url="https://example.com/model.glb")
        self.assertEqual(42, task_id)
        self.assertEqual("/lux3d/v1/part-split/task/create", gateway.requests[0]["path"])
        self.assertEqual({"glbUrl": "https://example.com/model.glb"}, gateway.requests[0]["body"])

    def test_task_list(self):
        gateway = FakeGateway()
        result = TasksResource(gateway, "com").list(
            page=2, page_size=10, status=3, start_time=100, end_time=200
        )
        self.assertEqual(0, result["total"])
        self.assertEqual("/global/lux3d/v1/generate/task/list", gateway.requests[0]["path"])
        self.assertEqual(
            {"page": 2, "pagesize": 10, "status": 3, "starttime": 100, "endtime": 200},
            gateway.requests[0]["query"],
        )

    def test_async_resources(self):
        async def run():
            gateway = AsyncFakeGateway()
            task_id = await AsyncPartSplitResource(gateway, "cn").create(
                glb_url="https://example.com/model.glb"
            )
            page = await AsyncTasksResource(gateway, "cn").list()
            self.assertEqual(42, task_id)
            self.assertEqual(0, page["total"])
            self.assertEqual({}, gateway.requests[1]["query"])

        asyncio.run(run())


if __name__ == "__main__":
    unittest.main()
