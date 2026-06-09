from __future__ import annotations

from typing import Any, Mapping, Optional


class AholoError(Exception):
    def __init__(
        self,
        message: str,
        *,
        status_code: Optional[int] = None,
        status: Optional[str] = None,
        biz_code: Optional[str] = None,
        body: Any = None,
    ) -> None:
        super().__init__(message)
        self.status_code = status_code
        self.status = status
        self.biz_code = biz_code
        self.body = body


class AuthenticationError(AholoError):
    pass


class RateLimitError(AholoError):
    pass


class BusinessError(AholoError):
    def __init__(
        self,
        message: str,
        *,
        cmd_code: Optional[str] = None,
        status_code: Optional[int] = None,
        status: Optional[str] = None,
        biz_code: Optional[str] = None,
        body: Any = None,
    ) -> None:
        super().__init__(message, status_code=status_code, status=status, biz_code=biz_code, body=body)
        self.cmd_code = cmd_code


class PollingTimeoutError(AholoError):
    def __init__(self, message: str, *, timeout_ms: int, **kwargs: Any) -> None:
        super().__init__(message, **kwargs)
        self.timeout_ms = timeout_ms


class PollingFailedError(AholoError):
    pass


def _is_api_error_body(body: Any) -> bool:
    return isinstance(body, Mapping) and "status" in body and "message" in body


def _is_cmd_envelope(body: Any) -> bool:
    return isinstance(body, Mapping) and "c" in body


def throw_for_gateway_response(status_code: int, body: Any) -> None:
    if _is_api_error_body(body):
        details = body.get("details") or {}
        meta = details.get("metaData") or {}
        biz_code = meta.get("bizCode")
        message = body.get("message") or "Gateway request failed"
        kwargs = {"status_code": status_code, "status": body.get("status"), "biz_code": biz_code, "body": body}
        if status_code == 401 or body.get("status") == "UNAUTHENTICATED":
            raise AuthenticationError(message, **kwargs)
        if status_code == 429 or body.get("status") == "RESOURCE_EXHAUSTED":
            raise RateLimitError(message, **kwargs)
        raise AholoError(message, **kwargs)
    raise AholoError(f"HTTP {status_code}", status_code=status_code, body=body)


def assert_cmd_ok(body: Mapping[str, Any], context: str) -> None:
    if body.get("c") != "0":
        raise BusinessError(body.get("m") or f"{context} failed (c={body.get('c')})", cmd_code=body.get("c"), body=body)


def assert_cmd_success(body: Mapping[str, Any], context: str) -> Any:
    assert_cmd_ok(body, context)
    if body.get("d") is None:
        raise BusinessError(f"{context} succeeded but response data is empty", cmd_code=body.get("c"), body=body)
    return body["d"]


def throw_for_http_status(status_code: int, body: Any, context: str) -> None:
    if _is_cmd_envelope(body):
        assert_cmd_success(body, context)
    throw_for_gateway_response(status_code, body)
