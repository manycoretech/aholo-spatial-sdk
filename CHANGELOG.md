# Changelog

All notable changes to this project are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.2.0] - 2026-06-11

### Added

- **World API**: `useMask` on reconstruction/generation create params; `imagery`, `semanticsMetadata`, and `lodMetaPath` on world splat URLs (TypeScript, Python, Java).
- **Python**: Async clients for all packages — `create_async_world_client`, `create_async_lux3d_client`, `AsyncAssetClient`, plus `poll_until_async` in core.
- **Java**: Getters on Lux3D and World create-param builders; `EncodingSupport` for multipart encoding.

### Changed

- Synced World OpenAPI spec across TypeScript, Python, and Java.
- Bumped all packages (`@manycore/aholo-sdk-*`, `manycore-aholo-sdk-*`, `com.manycoreapis:aholo-sdk-*`) to **1.2.0**.
- **Java core**: Refactored `BaseHttpClient`, `MultipartBody`, and error handling; expanded `AholoClientConfig` (region, timeouts).
- **Asset**: Extended `UploadResult` fields; upload progress listener support in Java.

## [1.1.0] - 2026-06-09

### Added

- Initial public release of Aholo Spatial SDK for **TypeScript**, **Python**, and **Java**.
- Four packages per language: **core**, **asset**, **lux3d**, **world**.
- Stainless-style API surface: factory clients, resource methods, `AHOLO_API_KEY` auth, layered exceptions.
- **Asset**: Single-file and multipart upload with progress callbacks.
- **Lux3D**: Image-to-3D, text-to-3D, material transfer, task polling.
- **World**: 3DGS reconstruction and generation, `waitFor` / poll helpers.
- Bilingual READMEs (EN / zh-CN) and runnable examples per language.
- Publish scripts for npm, PyPI, and Maven Central.

## [1.0.0] - 2026-05-29

### Added

- Repository initialized.

[1.2.0]: https://github.com/manycoretech/aholo-spatial-sdk/compare/world/v1.1.0...dae395e
[1.1.0]: https://github.com/manycoretech/aholo-spatial-sdk/compare/1c27d39...d9b09f2
[1.0.0]: https://github.com/manycoretech/aholo-spatial-sdk/commit/1c27d39
