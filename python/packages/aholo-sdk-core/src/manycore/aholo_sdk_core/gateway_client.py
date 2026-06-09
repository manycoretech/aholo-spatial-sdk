from __future__ import annotations

from typing import Any, Mapping, Optional

from .config import AholoClientConfig, resolve_api_key, resolve_base_url
from .http import BaseHttpClient

DEFAULT_USER_AGENT = "aholo-sdk-core/1.1.0"


class AholoGatewayClient(BaseHttpClient):
    def __init__(self, config: Optional[AholoClientConfig] = None) -> None:
        config = config or AholoClientConfig()
        api_key = resolve_api_key(config)
        user_agent = f"{DEFAULT_USER_AGENT} {config.user_agent}" if config.user_agent else DEFAULT_USER_AGENT
        super().__init__(
            base_url=resolve_base_url(config),
            timeout_ms=config.timeout_ms,
            headers={"Authorization": api_key},
            user_agent=user_agent,
        )
        self.api_key = api_key

    def gateway_request(
        self,
        *,
        method: str = "GET",
        path: str,
        query: Optional[Mapping[str, Any]] = None,
        body: Any = None,
        headers: Optional[Mapping[str, str]] = None,
        timeout_ms: Optional[int] = None,
    ) -> Any:
        return self.request(method=method, path=path, query=query, json_body=body, headers=headers, timeout_ms=timeout_ms)


def create_gateway_client(config: Optional[AholoClientConfig] = None) -> AholoGatewayClient:
    return AholoGatewayClient(config)
