from __future__ import annotations

import os
from dataclasses import dataclass
from typing import Literal, Optional

AholoRegion = Literal["cn", "com"]

DEFAULT_BASE_URL_CN = "https://api.aholo3d.cn"
DEFAULT_BASE_URL_GLOBAL = "https://api.aholo3d.com"
DEFAULT_TIMEOUT_MS = 60_000


@dataclass
class AholoClientConfig:
    api_key: Optional[str] = None
    base_url: Optional[str] = None
    region: AholoRegion = "cn"
    timeout_ms: int = DEFAULT_TIMEOUT_MS
    user_agent: Optional[str] = None


def resolve_base_url(config: AholoClientConfig) -> str:
    if config.base_url:
        return config.base_url.rstrip("/")
    if config.region == "com":
        return DEFAULT_BASE_URL_GLOBAL
    return DEFAULT_BASE_URL_CN


def resolve_api_key(config: AholoClientConfig) -> str:
    key = config.api_key or os.environ.get("AHOLO_API_KEY")
    if not key:
        raise ValueError("Missing API key: set api_key in config or AHOLO_API_KEY env var.")
    return key
