from __future__ import annotations

import hashlib
import threading
from concurrent.futures import ThreadPoolExecutor, as_completed
from dataclasses import dataclass
from pathlib import Path
from typing import Any, Callable, Mapping, Optional

from manycore.aholo_sdk_core import (
    AholoClientConfig,
    OusHttpClient,
    assert_cmd_ok,
    assert_cmd_success,
    create_gateway_client,
    poll_until,
)

OUS_STATUS_SUCCESS = 5
OUS_TERMINAL_FAILURE = {6, 8}
DEFAULT_PART_CONCURRENCY = 2
DEFAULT_POLL_INTERVAL_MS = 300
DEFAULT_POLL_TIMEOUT_MS = 300_000
DEFAULT_UPLOAD_PART_TIMEOUT_MS = 120_000  # 2 min per block; 1MB should upload in seconds, this covers server-side slow responses


def _token_path(region: str) -> str:
    return "/global/asset/v1/token" if region == "com" else "/asset/v1/token"


def _parse_lack_blocks(lack_blocks: list) -> set:
    """Parse OUS lackBlocks into a set of 1-based block numbers.
    Entries can be individual numbers ("5") or inclusive ranges ("72-423").
    """
    result = set()
    for item in lack_blocks:
        s = str(item)
        if "-" in s:
            start, end = s.split("-", 1)
            result.update(range(int(start), int(end) + 1))
        else:
            result.add(int(s))
    return result


def md5_hex(data: bytes) -> str:
    return hashlib.md5(data).hexdigest()


@dataclass
class UploadResult:
    url: str
    md5: str
    upload_key: Optional[str] = None
    obs_task_id: Optional[str] = None


class AssetClient:
    def __init__(self, config: Optional[AholoClientConfig] = None) -> None:
        self.config = config or AholoClientConfig()
        self.gateway = create_gateway_client(self.config)

    def get_upload_token(self) -> Mapping[str, Any]:
        return self.gateway.gateway_request(method="GET", path=_token_path(self.config.region))

    def upload_bytes(self, data: bytes, *, filename: str = "upload.bin", **options: Any) -> UploadResult:
        token = self.get_upload_token()
        ous = OusHttpClient(
            base_url=token["globalDomain"],
            ous_token=token["ousToken"],
            timeout_ms=self.config.timeout_ms,
        )
        digest = md5_hex(data)
        block_size = int(token["blockSize"])

        on_progress: Optional[Callable[[int, int], None]] = options.get("on_progress")
        if len(data) <= block_size:
            self._single_upload(ous, data, digest, filename, on_progress)
        else:
            self._block_upload(ous, data, digest, filename, block_size, options, on_progress)

        return self._poll_status(ous, options)

    def upload_file(self, file_path: str | Path, **options: Any) -> UploadResult:
        path = Path(file_path)
        data = path.read_bytes()
        return self.upload_bytes(data, filename=options.pop("filename", path.name), **options)

    def _single_upload(
        self,
        ous: OusHttpClient,
        data: bytes,
        digest: str,
        filename: str,
        on_progress: Optional[Callable[[int, int], None]] = None,
    ) -> None:
        body = ous.request(
            method="POST",
            path="/ous/api/v2/single/upload",
            files={"md5": (None, digest), "file": (filename, data, "application/octet-stream")},
        )
        assert_cmd_ok(body, "single upload")
        if on_progress:
            on_progress(len(data), len(data))

    def _block_upload(
        self,
        ous: OusHttpClient,
        data: bytes,
        digest: str,
        filename: str,
        block_size: int,
        options: Mapping[str, Any],
        on_progress: Optional[Callable[[int, int], None]] = None,
    ) -> None:
        parts = [data[i : i + block_size] for i in range(0, len(data), block_size)]
        init_body = ous.request(
            method="POST",
            path="/ous/api/v2/block/upload/init",
            query={
                "md5": digest,
                "blocks": len(parts),
                "size": len(data),
                "name": filename,
                "metadata": options.get("metadata"),
                "customPrefix": options.get("custom_prefix"),
                "customFilename": options.get("custom_filename"),
            },
        )
        init_data = assert_cmd_success(init_body, "block upload init")
        if init_data.get("deduplicated"):
            return

        # Filter to only the blocks that still need uploading.
        # lackBlocks = None means all blocks are needed (fresh upload).
        # lackBlocks = [] means nothing left to upload (all blocks already received).
        indexed_parts = list(enumerate(parts))
        lack_blocks = init_data.get("lackBlocks")
        if lack_blocks is not None:
            lack_set = _parse_lack_blocks(lack_blocks)
            indexed_parts = [(i, chunk) for i, chunk in indexed_parts if (i + 1) in lack_set]

        if not indexed_parts:
            return

        part_timeout_ms = options.get("part_timeout_ms", DEFAULT_UPLOAD_PART_TIMEOUT_MS)
        total = len(data)
        uploaded_bytes = 0
        lock = threading.Lock() if on_progress else None

        def upload_part(index: int, chunk: bytes) -> None:
            nonlocal uploaded_bytes
            part_body = ous.request(
                method="POST",
                path="/ous/api/v2/block/upload/part",
                files={
                    "block": (None, str(index + 1)),
                    "file": (f"{filename}.part{index + 1}", chunk, "application/octet-stream"),
                },
                timeout_ms=part_timeout_ms,
            )
            assert_cmd_ok(part_body, f"block upload part {index + 1}")
            if on_progress:
                with lock:
                    uploaded_bytes += len(chunk)
                    on_progress(uploaded_bytes, total)

        with ThreadPoolExecutor(max_workers=DEFAULT_PART_CONCURRENCY) as pool:
            futures = [pool.submit(upload_part, i, chunk) for i, chunk in indexed_parts]
            for future in as_completed(futures):
                future.result()

    def _poll_status(self, ous: OusHttpClient, options: Mapping[str, Any]) -> UploadResult:
        poll_opts = options.get("poll") or {}
        status = poll_until(
            lambda: assert_cmd_success(
                ous.request(method="GET", path="/ous/api/v2/upload/status"), "upload status"
            ),
            is_done=lambda d: d.get("status") == OUS_STATUS_SUCCESS and bool(d.get("url")),
            is_failed=lambda d: d.get("status") in OUS_TERMINAL_FAILURE,
            fail_message=lambda d: f"Upload failed status={d.get('status')} errorCode={d.get('errorCode')}",
            interval_ms=poll_opts.get("interval_ms", DEFAULT_POLL_INTERVAL_MS),
            timeout_ms=poll_opts.get("timeout_ms", DEFAULT_POLL_TIMEOUT_MS),
        )
        return UploadResult(
            url=status["url"],
            md5=status.get("md5") or "",
            upload_key=status.get("uploadKey"),
            obs_task_id=status.get("obsTaskId"),
        )


def create_asset_client(config: Optional[AholoClientConfig] = None) -> AssetClient:
    return AssetClient(config)
