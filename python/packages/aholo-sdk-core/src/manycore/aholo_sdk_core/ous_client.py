from __future__ import annotations

from .http import BaseHttpClient

OUS_TOKEN_HEADER = "ous-token-v2"


class OusHttpClient(BaseHttpClient):
    def __init__(self, *, base_url: str, ous_token: str, timeout_ms: int = 60_000) -> None:
        super().__init__(base_url=base_url, timeout_ms=timeout_ms, headers={OUS_TOKEN_HEADER: ous_token})
        self.ous_token = ous_token
