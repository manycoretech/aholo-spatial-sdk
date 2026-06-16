from __future__ import annotations

from typing import Any, Mapping, MutableMapping, Optional
from urllib.parse import urlencode, urljoin

import httpx

from .errors import throw_for_http_status


class BaseHttpClient:
    def __init__(
        self,
        *,
        base_url: str,
        timeout_ms: int = 60_000,
        headers: Optional[Mapping[str, str]] = None,
        user_agent: Optional[str] = None,
        client: Optional[httpx.Client] = None,
    ) -> None:
        self.base_url = base_url.rstrip("/")
        self.timeout_ms = timeout_ms
        self.default_headers: dict[str, str] = dict(headers or {})
        if user_agent:
            self.default_headers["User-Agent"] = user_agent
        self._client = client
        self._owns_client = client is None

    def _get_client(self) -> httpx.Client:
        if self._client is None:
            self._client = httpx.Client(timeout=self.timeout_ms / 1000.0)
        return self._client

    def close(self) -> None:
        if self._owns_client and self._client is not None:
            self._client.close()
            self._client = None

    def __enter__(self) -> "BaseHttpClient":
        return self

    def __exit__(self, *args: object) -> None:
        self.close()

    def request(
        self,
        *,
        method: str = "GET",
        path: str,
        query: Optional[Mapping[str, Any]] = None,
        headers: Optional[Mapping[str, str]] = None,
        json_body: Any = None,
        data: Any = None,
        files: Any = None,
        timeout_ms: Optional[int] = None,
    ) -> Any:
        url = urljoin(self.base_url + "/", path.lstrip("/"))
        if query:
            params = {k: v for k, v in query.items() if v is not None}
            if params:
                url = f"{url}?{urlencode(params)}"

        req_headers: MutableMapping[str, str] = dict(self.default_headers)
        if headers:
            req_headers.update({k: v for k, v in headers.items() if v is not None})

        if json_body is not None and "Content-Type" not in req_headers and "content-type" not in req_headers:
            req_headers["Content-Type"] = "application/json"

        client = self._get_client()
        try:
            response = client.request(
                method=method,
                url=url,
                headers=req_headers,
                json=json_body,
                data=data,
                files=files,
                timeout=(timeout_ms or self.timeout_ms) / 1000.0,
            )
        except httpx.TimeoutException as exc:
            raise TimeoutError(
                f"Request timed out after {timeout_ms or self.timeout_ms}ms: {method} {path}"
            ) from exc

        content_type = response.headers.get("content-type", "")
        if "application/json" in content_type:
            parsed: Any = response.json()
        else:
            text = response.text
            if not text:
                parsed = None
            else:
                try:
                    parsed = response.json()
                except ValueError:
                    parsed = text

        if response.status_code >= 400:
            throw_for_http_status(response.status_code, parsed, f"{method} {path}")
        return parsed


class AsyncBaseHttpClient:
    def __init__(
        self,
        *,
        base_url: str,
        timeout_ms: int = 60_000,
        headers: Optional[Mapping[str, str]] = None,
        user_agent: Optional[str] = None,
        client: Optional[httpx.AsyncClient] = None,
    ) -> None:
        self.base_url = base_url.rstrip("/")
        self.timeout_ms = timeout_ms
        self.default_headers: dict[str, str] = dict(headers or {})
        if user_agent:
            self.default_headers["User-Agent"] = user_agent
        self._client = client
        self._owns_client = client is None

    def _get_client(self) -> httpx.AsyncClient:
        if self._client is None:
            self._client = httpx.AsyncClient(timeout=self.timeout_ms / 1000.0)
        return self._client

    async def close(self) -> None:
        if self._owns_client and self._client is not None:
            await self._client.aclose()
            self._client = None

    async def __aenter__(self) -> "AsyncBaseHttpClient":
        return self

    async def __aexit__(self, *args: object) -> None:
        await self.close()

    async def request(
        self,
        *,
        method: str = "GET",
        path: str,
        query: Optional[Mapping[str, Any]] = None,
        headers: Optional[Mapping[str, str]] = None,
        json_body: Any = None,
        data: Any = None,
        files: Any = None,
        timeout_ms: Optional[int] = None,
    ) -> Any:
        url = urljoin(self.base_url + "/", path.lstrip("/"))
        if query:
            params = {k: v for k, v in query.items() if v is not None}
            if params:
                url = f"{url}?{urlencode(params)}"

        req_headers: MutableMapping[str, str] = dict(self.default_headers)
        if headers:
            req_headers.update({k: v for k, v in headers.items() if v is not None})

        if json_body is not None and "Content-Type" not in req_headers and "content-type" not in req_headers:
            req_headers["Content-Type"] = "application/json"

        client = self._get_client()
        try:
            response = await client.request(
                method=method,
                url=url,
                headers=req_headers,
                json=json_body,
                data=data,
                files=files,
                timeout=(timeout_ms or self.timeout_ms) / 1000.0,
            )
        except httpx.TimeoutException as exc:
            raise TimeoutError(
                f"Request timed out after {timeout_ms or self.timeout_ms}ms: {method} {path}"
            ) from exc

        content_type = response.headers.get("content-type", "")
        if "application/json" in content_type:
            parsed: Any = response.json()
        else:
            text_body = response.text
            if not text_body:
                parsed = None
            else:
                try:
                    parsed = response.json()
                except ValueError:
                    parsed = text_body

        if response.status_code >= 400:
            throw_for_http_status(response.status_code, parsed, f"{method} {path}")
        return parsed
