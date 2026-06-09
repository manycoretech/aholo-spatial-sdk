def lux3d_path(region: str, suffix: str) -> str:
    prefix = "/global/lux3d/v1" if region == "com" else "/lux3d/v1"
    return prefix + (suffix if suffix.startswith("/") else f"/{suffix}")
