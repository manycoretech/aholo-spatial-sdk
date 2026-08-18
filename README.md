# Aholo Spatial SDK

[中文文档](README.zh-CN.md)

Official SDKs for the [Aholo](https://labs.aholo3d.com) Open API — asset upload, Lux3D generation/part split/task history, and 3DGS world reconstruction.

> 📖 **Full documentation (canonical)**: [https://labs.aholo3d.com/api-docs/en/sdk](https://labs.aholo3d.com/api-docs/en/sdk)

## Packages

| Language | Package | Version | Install |
|----------|---------|---------|---------|
| TypeScript / Node.js | `@manycore/aholo-sdk-asset` | [![npm](https://img.shields.io/npm/v/@manycore/aholo-sdk-asset?color=CB3837&logo=npm)](https://www.npmjs.com/package/@manycore/aholo-sdk-asset) | `npm install @manycore/aholo-sdk-asset` |
| TypeScript / Node.js | `@manycore/aholo-sdk-lux3d` | [![npm](https://img.shields.io/npm/v/@manycore/aholo-sdk-lux3d?color=CB3837&logo=npm)](https://www.npmjs.com/package/@manycore/aholo-sdk-lux3d) | `npm install @manycore/aholo-sdk-lux3d` |
| TypeScript / Node.js | `@manycore/aholo-sdk-world` | [![npm](https://img.shields.io/npm/v/@manycore/aholo-sdk-world?color=CB3837&logo=npm)](https://www.npmjs.com/package/@manycore/aholo-sdk-world) | `npm install @manycore/aholo-sdk-world` |
| TypeScript / Node.js | `@manycore/aholo-sdk-core` | [![npm](https://img.shields.io/npm/v/@manycore/aholo-sdk-core?color=CB3837&logo=npm)](https://www.npmjs.com/package/@manycore/aholo-sdk-core) | `npm install @manycore/aholo-sdk-core` |
| Python | `manycore-aholo-sdk-asset` | [![PyPI](https://img.shields.io/pypi/v/manycore-aholo-sdk-asset?color=3775A9&logo=pypi&logoColor=white)](https://pypi.org/project/manycore-aholo-sdk-asset/) | `pip install manycore-aholo-sdk-asset` |
| Python | `manycore-aholo-sdk-lux3d` | [![PyPI](https://img.shields.io/pypi/v/manycore-aholo-sdk-lux3d?color=3775A9&logo=pypi&logoColor=white)](https://pypi.org/project/manycore-aholo-sdk-lux3d/) | `pip install manycore-aholo-sdk-lux3d` |
| Python | `manycore-aholo-sdk-world` | [![PyPI](https://img.shields.io/pypi/v/manycore-aholo-sdk-world?color=3775A9&logo=pypi&logoColor=white)](https://pypi.org/project/manycore-aholo-sdk-world/) | `pip install manycore-aholo-sdk-world` |
| Python | `manycore-aholo-sdk-core` | [![PyPI](https://img.shields.io/pypi/v/manycore-aholo-sdk-core?color=3775A9&logo=pypi&logoColor=white)](https://pypi.org/project/manycore-aholo-sdk-core/) | `pip install manycore-aholo-sdk-core` |
| Java | `com.manycoreapis:aholo-sdk-asset` | [![Maven Central](https://img.shields.io/maven-central/v/com.manycoreapis/aholo-sdk-asset?color=C71A36&logo=apachemaven&logoColor=white)](https://central.sonatype.com/artifact/com.manycoreapis/aholo-sdk-asset) | see [java/README.md](java/README.md) |
| Java | `com.manycoreapis:aholo-sdk-lux3d` | [![Maven Central](https://img.shields.io/maven-central/v/com.manycoreapis/aholo-sdk-lux3d?color=C71A36&logo=apachemaven&logoColor=white)](https://central.sonatype.com/artifact/com.manycoreapis/aholo-sdk-lux3d) | see [java/README.md](java/README.md) |
| Java | `com.manycoreapis:aholo-sdk-world` | [![Maven Central](https://img.shields.io/maven-central/v/com.manycoreapis/aholo-sdk-world?color=C71A36&logo=apachemaven&logoColor=white)](https://central.sonatype.com/artifact/com.manycoreapis/aholo-sdk-world) | see [java/README.md](java/README.md) |
| Java | `com.manycoreapis:aholo-sdk-core` | [![Maven Central](https://img.shields.io/maven-central/v/com.manycoreapis/aholo-sdk-core?color=C71A36&logo=apachemaven&logoColor=white)](https://central.sonatype.com/artifact/com.manycoreapis/aholo-sdk-core) | see [java/README.md](java/README.md) |

## Authentication

Set the `AHOLO_API_KEY` environment variable:

```bash
export AHOLO_API_KEY=your_api_key_here
```

Or pass it directly in the config:

```typescript
const asset = createAssetClient({ apiKey: 'your_api_key_here', region: 'com' });
```

> ⚠️ Never hardcode your API Key in source code. Use environment variables or a secrets manager in production.

Apply for your API Key:

- China: [https://labs.aholo3d.cn](https://labs.aholo3d.cn)
- Global: [https://labs.aholo3d.com](https://labs.aholo3d.com)

## Region

| Value | Description | Endpoint |
|-------|-------------|----------|
| `cn` | China | `https://api.aholo3d.cn` |
| `com` | Global | `https://api.aholo3d.com` |

## Quick Start

### TypeScript

**Install:**

```bash
npm install @manycore/aholo-sdk-asset @manycore/aholo-sdk-world
```

```typescript
import { createAssetClient } from '@manycore/aholo-sdk-asset';
import { createWorldClient } from '@manycore/aholo-sdk-world';

const asset = createAssetClient({ region: 'com' });
const world = createWorldClient({ region: 'com' });

const uploaded = await asset.uploadFile('./room.mp4');
const { worldId } = await world.reconstructions.create({
  name: 'My scene',
  resources: [{ url: uploaded.url, type: 'video' }],
  taskQuality: 'normal',
  scene: 'model',
});
const result = await world.waitFor(worldId);
console.log(result);
```

### Python

**Install:**

```bash
pip install manycore-aholo-sdk-asset manycore-aholo-sdk-world
```

```python
from manycore.aholo_sdk_asset import create_asset_client
from manycore.aholo_sdk_world import create_world_client

asset = create_asset_client()
world = create_world_client()

uploaded = asset.upload_file('room.mp4')
created = world.reconstructions.create(
    resources=[{'url': uploaded.url, 'type': 'video'}],
    task_quality='normal',
    scene='model',
    name='My scene',
)
result = world.wait_for(created["worldId"])
print(result)
```

### Java

**Requirements:** Java 8+ (build with JDK 17 or 21 recommended). See [java/README.md](java/README.md).

**Maven (`pom.xml`):**

> Replace `LATEST_VERSION` with the version shown in the Maven Central badge above.

```xml
<dependency>
  <groupId>com.manycoreapis</groupId>
  <artifactId>aholo-sdk-asset</artifactId>
  <version>LATEST_VERSION</version>
</dependency>
<dependency>
  <groupId>com.manycoreapis</groupId>
  <artifactId>aholo-sdk-world</artifactId>
  <version>LATEST_VERSION</version>
</dependency>
```

```java
import com.manycoreapis.sdk.asset.AssetClient;
import com.manycoreapis.sdk.asset.UploadResult;
import com.manycoreapis.sdk.core.AholoClientConfig;
import com.manycoreapis.sdk.world.WorldClient;
import com.manycoreapis.sdk.world.model.ReconstructionCreateParams;
import com.manycoreapis.sdk.world.model.WorldAsyncOperation;
import com.manycoreapis.sdk.world.model.WorldDetail;
import java.nio.file.Paths;

AssetClient asset = AssetClient.create(AholoClientConfig.ofRegion("com"));
WorldClient world = WorldClient.create(AholoClientConfig.ofRegion("com"));

UploadResult uploaded = asset.uploadFile(Paths.get("room.mp4"));
WorldAsyncOperation created = world.reconstructions().create(
    ReconstructionCreateParams.builder()
        .name("My scene")
        .addResource(uploaded.url(), "video")
        .taskQuality("normal")
        .scene("model")
        .build());
WorldDetail result = world.waitFor(created.worldId());
System.out.println(result);
```

### World input resources

| API | Resource `type` | Formats |
|-----|-----------------|---------|
| `reconstructions.create` | `image` \| `video` \| `insv` | Images: ≥20 × `.jpg`/`.jpeg`/`.png`/`.webp`; video: `.mp4`/`.mov`; Insta360: `.insv` |
| `generations.create` | `image` only | At most 1 image; combine with `prompt` |

Generation with a reference image (use `image`, not `video`/`insv`):

```typescript
await world.generations.create({
  prompt: 'Modern living room',
  resources: [{ url: uploaded.url, type: 'image' }],
});
```

```python
world.generations.create(
    prompt='Modern living room',
    resources=[{'url': uploaded.url, 'type': 'image'}],
)
```

```java
import com.manycoreapis.sdk.world.model.GenerationCreateParams;
import com.manycoreapis.sdk.world.model.GenerateWorldResource;

world.generations().create(
    GenerationCreateParams.builder()
        .prompt("Modern living room")
        .addResource(GenerateWorldResource.of(uploaded.url()))
        .build());
```

## Error Handling

The SDK throws typed errors:

| Error | Description |
|-------|-------------|
| `AuthenticationError` | Invalid or missing API Key |
| `RateLimitError` | Request rate limit exceeded |
| `BusinessError` | API business error (includes error code) |
| `PollingTimeoutError` | Task polling timed out |
| `PollingFailedError` | Task execution failed |

```typescript
import { AuthenticationError, BusinessError } from '@manycore/aholo-sdk-core';

try {
  const result = await world.waitFor(worldId);
} catch (e) {
  if (e instanceof AuthenticationError) {
    console.error('Invalid API Key');
  } else if (e instanceof BusinessError) {
    console.error('Business error:', e.code, e.message);
  }
}
```

## Upload Progress

Asset uploads support a progress callback:

```typescript
const uploaded = await asset.uploadFile('./room.mp4', {
  onProgress: (uploaded, total) => {
    const pct = Math.round((uploaded / total) * 100);
    process.stdout.write(`\rUploading: ${pct}%`);
  },
});
```

## Examples

See full examples for each language:

- TypeScript: [typescript/examples/](typescript/examples/)
- Python: [python/examples/](python/examples/)
- Java: [java/examples/](java/examples/)

## License

[MIT](LICENSE) · [Changelog](CHANGELOG.md) · [Security](SECURITY.md)
