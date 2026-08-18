# Aholo Spatial SDK — Python

[English](README.md)

[Aholo](https://labs.aholo3d.cn) OpenAPI 官方 Python SDK。

> 📖 **完整文档（canonical）**：[https://labs.aholo3d.cn/api-docs/sdk/python](https://labs.aholo3d.cn/api-docs/sdk/python)

**运行要求：** Python ≥ 3.9

## 包列表

| 包名 | 版本 | 说明 |
|------|------|------|
| `manycore-aholo-sdk-asset` | [![PyPI](https://img.shields.io/pypi/v/manycore-aholo-sdk-asset?color=3775A9&logo=pypi&logoColor=white)](https://pypi.org/project/manycore-aholo-sdk-asset/) | 文件上传（单文件/分块上传，支持断点续传） |
| `manycore-aholo-sdk-lux3d` | [![PyPI](https://img.shields.io/pypi/v/manycore-aholo-sdk-lux3d?color=3775A9&logo=pypi&logoColor=white)](https://pypi.org/project/manycore-aholo-sdk-lux3d/) | Lux3D 生成、材质迁移、部件拆分、生成记录查询 |
| `manycore-aholo-sdk-world` | [![PyPI](https://img.shields.io/pypi/v/manycore-aholo-sdk-world?color=3775A9&logo=pypi&logoColor=white)](https://pypi.org/project/manycore-aholo-sdk-world/) | 3DGS 世界重建与生成 |
| `manycore-aholo-sdk-core` | [![PyPI](https://img.shields.io/pypi/v/manycore-aholo-sdk-core?color=3775A9&logo=pypi&logoColor=white)](https://pypi.org/project/manycore-aholo-sdk-core/) | 公共 HTTP 客户端、鉴权、错误类型、轮询 |

## 安装

按需安装对应包：

```bash
pip install manycore-aholo-sdk-asset   # 文件上传
pip install manycore-aholo-sdk-lux3d   # Lux3D 生成
pip install manycore-aholo-sdk-world   # 世界重建
```

本地开发可从源码 editable 安装，见 [examples/](examples/) 目录说明。

## 鉴权

```bash
export AHOLO_API_KEY=your_api_key_here
```

API Key 申请：[labs.aholo3d.cn](https://labs.aholo3d.cn)（中国区）· [labs.aholo3d.com](https://labs.aholo3d.com)（海外区）

## 区域

| 值 | 说明 | 接入点 |
|----|------|--------|
| `cn` | 中国区 | `https://api.aholo3d.cn` |
| `com` | 海外区 | `https://api.aholo3d.com` |

> API 用法与错误处理见 [完整文档](https://labs.aholo3d.cn/api-docs/sdk/python)。

## 示例代码

见 [examples/](examples/) 目录，运行方式见[完整文档](https://labs.aholo3d.cn/api-docs/sdk/python#示例代码)。

- `world_reconstruct.py` — `.mp4`/`.mov`（`video`）或 Insta360 `.insv`（`insv`）

## 许可证

[MIT](../LICENSE)
