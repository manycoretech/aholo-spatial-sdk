# Aholo Spatial SDK — TypeScript / Node.js

[中文文档](README.zh-CN.md)

Official TypeScript/Node.js SDKs for the [Aholo](https://labs.aholo3d.com) Open API.

> 📖 **Full documentation (canonical)**: [https://labs.aholo3d.com/api-docs/en/sdk/typescript](https://labs.aholo3d.com/api-docs/en/sdk/typescript)

**Requirements:** Node.js ≥ 18

## Packages

| Package | Version | Description |
|---------|---------|-------------|
| `@manycore/aholo-sdk-asset` | [![npm](https://img.shields.io/npm/v/@manycore/aholo-sdk-asset?color=CB3837&logo=npm)](https://www.npmjs.com/package/@manycore/aholo-sdk-asset) | File upload (single & multipart, resume support) |
| `@manycore/aholo-sdk-lux3d` | [![npm](https://img.shields.io/npm/v/@manycore/aholo-sdk-lux3d?color=CB3837&logo=npm)](https://www.npmjs.com/package/@manycore/aholo-sdk-lux3d) | Lux3D image/text-to-3D generation, material transfer |
| `@manycore/aholo-sdk-world` | [![npm](https://img.shields.io/npm/v/@manycore/aholo-sdk-world?color=CB3837&logo=npm)](https://www.npmjs.com/package/@manycore/aholo-sdk-world) | 3DGS world reconstruction & generation |
| `@manycore/aholo-sdk-core` | [![npm](https://img.shields.io/npm/v/@manycore/aholo-sdk-core?color=CB3837&logo=npm)](https://www.npmjs.com/package/@manycore/aholo-sdk-core) | Shared HTTP client, auth, error types, polling |

## Installation

Install only the packages you need:

```bash
npm install @manycore/aholo-sdk-asset   # asset upload
npm install @manycore/aholo-sdk-lux3d   # Lux3D generation
npm install @manycore/aholo-sdk-world   # world reconstruction
```

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

> API usage and error handling: see [full documentation](https://labs.aholo3d.com/api-docs/en/sdk/typescript).

## Examples

See [examples/](examples/) for runnable scripts. Run instructions are in each script or the [full documentation](https://labs.aholo3d.com/api-docs/en/sdk/typescript).

## License

[MIT](../LICENSE)
