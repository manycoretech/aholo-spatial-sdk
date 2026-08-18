# Aholo Spatial SDK — Python

[中文文档](README.zh-CN.md)

Official Python SDKs for the [Aholo](https://labs.aholo3d.com) Open API.

> 📖 **Full documentation (canonical)**: [https://labs.aholo3d.com/api-docs/en/sdk/python](https://labs.aholo3d.com/api-docs/en/sdk/python)

**Requirements:** Python ≥ 3.9

## Packages

| Package | Version | Description |
|---------|---------|-------------|
| `manycore-aholo-sdk-asset` | [![PyPI](https://img.shields.io/pypi/v/manycore-aholo-sdk-asset?color=3775A9&logo=pypi&logoColor=white)](https://pypi.org/project/manycore-aholo-sdk-asset/) | File upload (single & multipart, resume support) |
| `manycore-aholo-sdk-lux3d` | [![PyPI](https://img.shields.io/pypi/v/manycore-aholo-sdk-lux3d?color=3775A9&logo=pypi&logoColor=white)](https://pypi.org/project/manycore-aholo-sdk-lux3d/) | Lux3D generation, material transfer, part split, task history |
| `manycore-aholo-sdk-world` | [![PyPI](https://img.shields.io/pypi/v/manycore-aholo-sdk-world?color=3775A9&logo=pypi&logoColor=white)](https://pypi.org/project/manycore-aholo-sdk-world/) | 3DGS world reconstruction & generation |
| `manycore-aholo-sdk-core` | [![PyPI](https://img.shields.io/pypi/v/manycore-aholo-sdk-core?color=3775A9&logo=pypi&logoColor=white)](https://pypi.org/project/manycore-aholo-sdk-core/) | Shared HTTP client, auth, error types, polling |

## Installation

Install only the packages you need:

```bash
pip install manycore-aholo-sdk-asset   # asset upload
pip install manycore-aholo-sdk-lux3d   # Lux3D generation
pip install manycore-aholo-sdk-world   # world reconstruction
```

For local development, install editable from source — see [examples/](examples/).

## Authentication

```bash
export AHOLO_API_KEY=your_api_key_here
```

Apply for an API Key: [labs.aholo3d.cn](https://labs.aholo3d.cn) (China) · [labs.aholo3d.com](https://labs.aholo3d.com) (Global)

## Region

| Value | Description | Endpoint |
|-------|-------------|----------|
| `cn` | China | `https://api.aholo3d.cn` |
| `com` | Global | `https://api.aholo3d.com` |

> API usage and error handling: see [full documentation](https://labs.aholo3d.com/api-docs/en/sdk/python).

## Examples

See [examples/](examples/) for runnable scripts. Run instructions are in the [full documentation](https://labs.aholo3d.com/api-docs/en/sdk/python).

- `world_reconstruct.py` — `.mp4`/`.mov` (`video`) or Insta360 `.insv` (`insv`)

## License

[MIT](../LICENSE)
