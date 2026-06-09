# Aholo Spatial SDK — TypeScript / Node.js

[English](README.md)

[Aholo](https://labs.aholo3d.cn) OpenAPI 官方 TypeScript/Node.js SDK。

> 📖 **完整文档（canonical）**：[https://labs.aholo3d.cn/api-docs/sdk/typescript](https://labs.aholo3d.cn/api-docs/sdk/typescript)

**运行要求：** Node.js ≥ 18

## 包列表

| 包名 | 版本 | 说明 |
|------|------|------|
| `@manycore/aholo-sdk-asset` | [![npm](https://img.shields.io/npm/v/@manycore/aholo-sdk-asset?color=CB3837&logo=npm)](https://www.npmjs.com/package/@manycore/aholo-sdk-asset) | 文件上传（单文件/分块上传，支持断点续传） |
| `@manycore/aholo-sdk-lux3d` | [![npm](https://img.shields.io/npm/v/@manycore/aholo-sdk-lux3d?color=CB3837&logo=npm)](https://www.npmjs.com/package/@manycore/aholo-sdk-lux3d) | Lux3D 图像/文字转 3D 生成、材质迁移 |
| `@manycore/aholo-sdk-world` | [![npm](https://img.shields.io/npm/v/@manycore/aholo-sdk-world?color=CB3837&logo=npm)](https://www.npmjs.com/package/@manycore/aholo-sdk-world) | 3DGS 世界重建与生成 |
| `@manycore/aholo-sdk-core` | [![npm](https://img.shields.io/npm/v/@manycore/aholo-sdk-core?color=CB3837&logo=npm)](https://www.npmjs.com/package/@manycore/aholo-sdk-core) | 公共 HTTP 客户端、鉴权、错误类型、轮询 |

## 安装

按需安装对应包：

```bash
npm install @manycore/aholo-sdk-asset   # 文件上传
npm install @manycore/aholo-sdk-lux3d   # Lux3D 生成
npm install @manycore/aholo-sdk-world   # 世界重建
```

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

> API 用法与错误处理见 [完整文档](https://labs.aholo3d.cn/api-docs/sdk/typescript)。

## 示例代码

见 [examples/](examples/) 目录，运行方式见各脚本注释或[完整文档](https://labs.aholo3d.cn/api-docs/sdk/typescript#示例代码)。

## 许可证

[MIT](../LICENSE)
