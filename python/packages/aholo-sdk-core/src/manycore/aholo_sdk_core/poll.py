from __future__ import annotations

import time
from typing import Callable, Optional, TypeVar

from .errors import PollingFailedError, PollingTimeoutError

T = TypeVar("T")


def poll_until(
    fn: Callable[[], T],
    *,
    is_done: Callable[[T], bool],
    is_failed: Optional[Callable[[T], bool]] = None,
    fail_message: Optional[Callable[[T], str]] = None,
    interval_ms: int = 500,
    timeout_ms: int = 300_000,
) -> T:
    started = time.monotonic()
    while True:
        result = fn()
        if is_failed and is_failed(result):
            raise PollingFailedError(fail_message(result) if fail_message else "Polling failed")
        if is_done(result):
            return result
        if (time.monotonic() - started) * 1000 >= timeout_ms:
            raise PollingTimeoutError(f"Polling timed out after {timeout_ms}ms", timeout_ms=timeout_ms)
        time.sleep(interval_ms / 1000.0)
