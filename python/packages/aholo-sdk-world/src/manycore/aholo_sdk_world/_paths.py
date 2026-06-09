def world_path(region: str, suffix: str) -> str:
    prefix = "/global/world/v1" if region == "com" else "/world/v1"
    return prefix + (suffix if suffix.startswith("/") else f"/{suffix}")
