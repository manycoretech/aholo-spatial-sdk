from __future__ import annotations

from typing import TYPE_CHECKING, Optional, cast

from manycore.aholo_sdk_core import BusinessError, assert_cmd_success, poll_until

from .._paths import lux3d_path
from ..types import LUX3D_STATUS_FAILED, LUX3D_STATUS_SUCCESS, Lux3dTaskResult, TaskPagedList

if TYPE_CHECKING:
    from manycore.aholo_sdk_core import AholoGatewayClient

DEFAULT_POLL_INTERVAL_MS = 12_000
DEFAULT_POLL_TIMEOUT_MS = 600_000


class TasksResource:
    def __init__(self, gateway: AholoGatewayClient, region: str) -> None:
        self._gateway = gateway
        self._region = region

    def retrieve(self, task_id: int | str) -> Lux3dTaskResult:
        """GET /generate/task/get"""
        body = self._gateway.gateway_request(
            method="GET",
            path=lux3d_path(self._region, "/generate/task/get"),
            query={"taskid": str(task_id)},
        )
        data = assert_cmd_success(body, "tasks.retrieve")
        if data.get("taskId") is None or data.get("status") is None:
            raise BusinessError("Lux3D task query returned incomplete data", body=data)
        return Lux3dTaskResult(
            taskId=data["taskId"],
            status=data["status"],
            outputs=data.get("outputs") or [],
        )

    def wait_for(
        self,
        task_id: int | str,
        *,
        interval_ms: int = DEFAULT_POLL_INTERVAL_MS,
        timeout_ms: int = DEFAULT_POLL_TIMEOUT_MS,
    ) -> Lux3dTaskResult:
        """Poll task until success or failure."""
        return poll_until(
            lambda: self.retrieve(task_id),
            is_done=lambda r: r.get("status") == LUX3D_STATUS_SUCCESS,
            is_failed=lambda r: r.get("status") == LUX3D_STATUS_FAILED,
            fail_message=lambda r: f"Lux3D task failed taskId={task_id} status={r.get('status')}",
            interval_ms=interval_ms,
            timeout_ms=timeout_ms,
        )

    def list(
        self,
        *,
        page: Optional[int] = None,
        page_size: Optional[int] = None,
        status: Optional[int] = None,
        start_time: Optional[int] = None,
        end_time: Optional[int] = None,
    ) -> TaskPagedList:
        """GET /generate/task/list. Omitted filters are not sent."""
        query: dict = {}
        if page is not None:
            query["page"] = page
        if page_size is not None:
            query["pagesize"] = page_size
        if status is not None:
            query["status"] = status
        if start_time is not None:
            query["starttime"] = start_time
        if end_time is not None:
            query["endtime"] = end_time
        body = self._gateway.gateway_request(
            method="GET",
            path=lux3d_path(self._region, "/generate/task/list"),
            query=query,
        )
        return cast(TaskPagedList, assert_cmd_success(body, "tasks.list"))

from manycore.aholo_sdk_core import AsyncAholoGatewayClient, BusinessError, assert_cmd_success, poll_until_async

from .._paths import lux3d_path
from ..types import LUX3D_STATUS_FAILED, LUX3D_STATUS_SUCCESS, Lux3dTaskResult, TaskPagedList


class AsyncTasksResource:
    def __init__(self, gateway: AsyncAholoGatewayClient, region: str) -> None:
        self._gateway = gateway
        self._region = region

    async def retrieve(self, task_id: int | str) -> Lux3dTaskResult:
        body = await self._gateway.gateway_request(
            method="GET",
            path=lux3d_path(self._region, "/generate/task/get"),
            query={"taskid": str(task_id)},
        )
        data = assert_cmd_success(body, "tasks.retrieve")
        if data.get("taskId") is None or data.get("status") is None:
            raise BusinessError("Lux3D task query returned incomplete data", body=data)
        return Lux3dTaskResult(
            taskId=data["taskId"],
            status=data["status"],
            outputs=data.get("outputs") or [],
        )

    async def wait_for(
        self,
        task_id: int | str,
        *,
        interval_ms: int = DEFAULT_POLL_INTERVAL_MS,
        timeout_ms: int = DEFAULT_POLL_TIMEOUT_MS,
    ) -> Lux3dTaskResult:
        return await poll_until_async(
            lambda: self.retrieve(task_id),
            is_done=lambda r: r.get("status") == LUX3D_STATUS_SUCCESS,
            is_failed=lambda r: r.get("status") == LUX3D_STATUS_FAILED,
            fail_message=lambda r: f"Lux3D task failed taskId={task_id} status={r.get('status')}",
            interval_ms=interval_ms,
            timeout_ms=timeout_ms,
        )

    async def list(
        self,
        *,
        page: Optional[int] = None,
        page_size: Optional[int] = None,
        status: Optional[int] = None,
        start_time: Optional[int] = None,
        end_time: Optional[int] = None,
    ) -> TaskPagedList:
        query: dict = {}
        if page is not None:
            query["page"] = page
        if page_size is not None:
            query["pagesize"] = page_size
        if status is not None:
            query["status"] = status
        if start_time is not None:
            query["starttime"] = start_time
        if end_time is not None:
            query["endtime"] = end_time
        body = await self._gateway.gateway_request(
            method="GET",
            path=lux3d_path(self._region, "/generate/task/list"),
            query=query,
        )
        return cast(TaskPagedList, assert_cmd_success(body, "tasks.list"))
