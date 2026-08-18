# Changelog

All notable changes to this project are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Lux3D 1.5.0] - 2026-08-18

### Added

- **Lux3D API**: GLB part-split task creation via `partSplit.create` / `part_split.create`.
- **Lux3D API**: Paginated generation records via `tasks.list`, with status and creation-time filters.
- **TypeScript / Python / Java**: Models and resource methods for part split and task history; Python includes synchronous and asynchronous variants.
- Cross-language tests for endpoint paths, query mapping, and response decoding.

### Changed

- Bumped Lux3D packages only (`@manycore/aholo-sdk-lux3d`, `manycore-aholo-sdk-lux3d`, `com.manycoreapis:aholo-sdk-lux3d`) to **1.5.0**.
- Updated the documented G1 default `textureSize` from 1000 to 2048.

## [Lux3D 1.4.0] - 2026-07-20

### Added

- **Lux3D API**: Version `G1` (beta) for image/text-to-3D; multi-view input via `imgs` (1–32).
- **Lux3D API**: G1 options `enablePbr` / `textureSize`; G1 outputs `zip` / `glb` / `ply`.
- **TypeScript / Python / Java**: `createFromFiles` / `create_from_files` helpers for local multi-view uploads.
- **TypeScript / Python**: `Lux3dOutputFormat` type alias.

### Changed

- Synced Lux3D OpenAPI spec across TypeScript, Python, and Java.
- Bumped **lux3d** packages only (`@manycore/aholo-sdk-lux3d`, `manycore-aholo-sdk-lux3d`, `com.manycoreapis:aholo-sdk-lux3d`) to **1.4.0**.
- v2.0-preview task outputs align with v3 five-format slots; v1.0-pro returns a single ZIP (not `.lux3d`).
- `faceCount` applies to v2.0-preview, v3.0-standard, and G1.

### Breaking Changes

- Removed create-task params `needUsdz` / `needObj` / `needFbx` (and Python `need_usdz` / `need_obj` / `need_fbx`).
- Use `outputFormat` (TypeScript/Java) / `output_format` (Python) string array instead.

## [Lux3D 1.3.0] - 2026-07-08

### Added

- **Lux3D API**: Default model version `v3.0-standard`; version enum now includes `v3.0-standard`, `v2.0-preview`, and `v1.0-pro`.
- **Lux3D API**: Create-task params `faceCount`, `needUsdz`, `needObj`, `needFbx` (image/text-to-3D and material transfer).
- **Lux3D API**: v3.0-standard five-format task outputs (`.zip`, `.glb`, `.usdz`, `_obj.zip`, `_fbx.zip`); unrequested optional slots return `NOT_REQUESTED`.
- **Python**: `LUX3D_OUTPUT_NOT_REQUESTED` constant.

### Changed

- Synced Lux3D OpenAPI spec across TypeScript, Python, and Java.
- Bumped **lux3d** packages only (`@manycore/aholo-sdk-lux3d`, `manycore-aholo-sdk-lux3d`, `com.manycoreapis:aholo-sdk-lux3d`) to **1.3.0** (core, asset, world unchanged).
- Lux3D examples and package README updated for v3.0 output layout.

## [1.3.0] - 2026-06-29

### Added

- **World API**: Reconstruction supports `insv` (Insta360 panorama video, `.insv` extension) alongside `image` and `video`.
- **TypeScript / Python**: `GenerateWorldResourceItem` and `WorldGenResourceType` for generation tasks (reference images only).
- **Java**: `GenerateWorldResource` model for generation create params.

### Changed

- Synced World OpenAPI spec across TypeScript, Python, and Java; reconstruction and generation resource schemas are now separate types.
- Bumped **world** packages only (`@manycore/aholo-sdk-world`, `manycore-aholo-sdk-world`, `com.manycoreapis:aholo-sdk-world`) to **1.3.0** (core, asset, lux3d remain at 1.2.0).
- World reconstruct examples (TypeScript, Python, Java): auto-select `video` or `insv` from file extension.

### Breaking Changes

- **Java**: `GenerationCreateParams.resources` now uses `List<GenerateWorldResource>` instead of `List<WorldResource>` (generation accepts `image` only).

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

[lux3d-1.5.0]: https://github.com/manycoretech/aholo-spatial-sdk/compare/lux3d/v1.4.0...lux3d/v1.5.0
[lux3d-1.4.0]: https://github.com/manycoretech/aholo-spatial-sdk/compare/lux3d/v1.3.0...lux3d/v1.4.0
[lux3d-1.3.0]: https://github.com/manycoretech/aholo-spatial-sdk/compare/lux3d/v1.2.0...lux3d/v1.3.0
[1.3.0]: https://github.com/manycoretech/aholo-spatial-sdk/compare/world/v1.2.0...world/v1.3.0
[1.2.0]: https://github.com/manycoretech/aholo-spatial-sdk/compare/world/v1.1.0...dae395e
[1.1.0]: https://github.com/manycoretech/aholo-spatial-sdk/compare/1c27d39...d9b09f2
[1.0.0]: https://github.com/manycoretech/aholo-spatial-sdk/commit/1c27d39
