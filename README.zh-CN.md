# Aholo Spatial SDK

[Aholo](https://labs.aholo3d.cn) OpenAPI 官方 SDK，提供资产上传、Lux3D 生成/部件拆分/生成记录查询、3DGS 世界重建等能力，支持 TypeScript/Node.js、Python、Java 三种语言。

> 📖 **完整文档（canonical）**：[https://labs.aholo3d.cn/api-docs/sdk](https://labs.aholo3d.cn/api-docs/sdk)

## 包列表

| 语言 | 包名 | 版本 | 安装 |
|------|------|------|------|
| TypeScript / Node.js | `@manycore/aholo-sdk-asset` | [![npm](https://img.shields.io/npm/v/@manycore/aholo-sdk-asset?color=CB3837&logo=npm)](https://www.npmjs.com/package/@manycore/aholo-sdk-asset) | `npm install @manycore/aholo-sdk-asset` |
| TypeScript / Node.js | `@manycore/aholo-sdk-lux3d` | [![npm](https://img.shields.io/npm/v/@manycore/aholo-sdk-lux3d?color=CB3837&logo=npm)](https://www.npmjs.com/package/@manycore/aholo-sdk-lux3d) | `npm install @manycore/aholo-sdk-lux3d` |
| TypeScript / Node.js | `@manycore/aholo-sdk-world` | [![npm](https://img.shields.io/npm/v/@manycore/aholo-sdk-world?color=CB3837&logo=npm)](https://www.npmjs.com/package/@manycore/aholo-sdk-world) | `npm install @manycore/aholo-sdk-world` |
| TypeScript / Node.js | `@manycore/aholo-sdk-core` | [![npm](https://img.shields.io/npm/v/@manycore/aholo-sdk-core?color=CB3837&logo=npm)](https://www.npmjs.com/package/@manycore/aholo-sdk-core) | `npm install @manycore/aholo-sdk-core` |
| Python | `manycore-aholo-sdk-asset` | [![PyPI](https://img.shields.io/pypi/v/manycore-aholo-sdk-asset?color=3775A9&logo=pypi&logoColor=white)](https://pypi.org/project/manycore-aholo-sdk-asset/) | `pip install manycore-aholo-sdk-asset` |
| Python | `manycore-aholo-sdk-lux3d` | [![PyPI](https://img.shields.io/pypi/v/manycore-aholo-sdk-lux3d?color=3775A9&logo=pypi&logoColor=white)](https://pypi.org/project/manycore-aholo-sdk-lux3d/) | `pip install manycore-aholo-sdk-lux3d` |
| Python | `manycore-aholo-sdk-world` | [![PyPI](https://img.shields.io/pypi/v/manycore-aholo-sdk-world?color=3775A9&logo=pypi&logoColor=white)](https://pypi.org/project/manycore-aholo-sdk-world/) | `pip install manycore-aholo-sdk-world` |
| Python | `manycore-aholo-sdk-core` | [![PyPI](https://img.shields.io/pypi/v/manycore-aholo-sdk-core?color=3775A9&logo=pypi&logoColor=white)](https://pypi.org/project/manycore-aholo-sdk-core/) | `pip install manycore-aholo-sdk-core` |
| Java | `com.manycoreapis:aholo-sdk-asset` | [![Maven Central](https://img.shields.io/maven-central/v/com.manycoreapis/aholo-sdk-asset?color=C71A36&logo=apachemaven&logoColor=white)](https://central.sonatype.com/artifact/com.manycoreapis/aholo-sdk-asset) | 见 [java/README.md](java/README.md) |
| Java | `com.manycoreapis:aholo-sdk-lux3d` | [![Maven Central](https://img.shields.io/maven-central/v/com.manycoreapis/aholo-sdk-lux3d?color=C71A36&logo=apachemaven&logoColor=white)](https://central.sonatype.com/artifact/com.manycoreapis/aholo-sdk-lux3d) | 见 [java/README.md](java/README.md) |
| Java | `com.manycoreapis:aholo-sdk-world` | [![Maven Central](https://img.shields.io/maven-central/v/com.manycoreapis/aholo-sdk-world?color=C71A36&logo=apachemaven&logoColor=white)](https://central.sonatype.com/artifact/com.manycoreapis/aholo-sdk-world) | 见 [java/README.md](java/README.md) |
| Java | `com.manycoreapis:aholo-sdk-core` | [![Maven Central](https://img.shields.io/maven-central/v/com.manycoreapis/aholo-sdk-core?color=C71A36&logo=apachemaven&logoColor=white)](https://central.sonatype.com/artifact/com.manycoreapis/aholo-sdk-core) | 见 [java/README.md](java/README.md) |

## 鉴权

通过环境变量设置 API Key：

```bash
export AHOLO_API_KEY=your_api_key_here
```

或在代码中直接传入：

```typescript
const asset = createAssetClient({ apiKey: 'your_api_key_here', region: 'cn' });
```

> ⚠️ 请勿将 API Key 硬编码到源代码中，生产环境建议使用环境变量或密钥管理服务。

API Key 申请地址：

- 中国区：[https://labs.aholo3d.cn](https://labs.aholo3d.cn)
- 海外区：[https://labs.aholo3d.com](https://labs.aholo3d.com)

## 区域

| 值 | 说明 | 接入点 |
|----|------|--------|
| `cn` | 中国区 | `https://api.aholo3d.cn` |
| `com` | 海外区 | `https://api.aholo3d.com` |

## 快速开始

### TypeScript

**安装：**

```bash
npm install @manycore/aholo-sdk-asset @manycore/aholo-sdk-world
```

```typescript
import { createAssetClient } from '@manycore/aholo-sdk-asset';
import { createWorldClient } from '@manycore/aholo-sdk-world';

const asset = createAssetClient({ region: 'cn' });
const world = createWorldClient({ region: 'cn' });

// 上传视频文件
const uploaded = await asset.uploadFile('./room.mp4');

// 创建 3D 世界重建任务
const { worldId } = await world.reconstructions.create({
  name: '我的场景',
  resources: [{ url: uploaded.url, type: 'video' }],
  taskQuality: 'normal',
  scene: 'model',
});

// 等待任务完成
const result = await world.waitFor(worldId);
console.log(result);
```

### Python

**安装：**

```bash
pip install manycore-aholo-sdk-asset manycore-aholo-sdk-world
```

```python
from manycore.aholo_sdk_asset import create_asset_client
from manycore.aholo_sdk_world import create_world_client

asset = create_asset_client()
world = create_world_client()

# 上传视频文件
uploaded = asset.upload_file('room.mp4')

# 创建 3D 世界重建任务
created = world.reconstructions.create(
    resources=[{'url': uploaded.url, 'type': 'video'}],
    task_quality='normal',
    scene='model',
    name='我的场景',
)

# 等待任务完成
result = world.wait_for(created["worldId"])
print(result)
```

### Java

**运行要求：** Java 8+（推荐使用 JDK 17 或 21 构建）。详见 [java/README.zh-CN.md](java/README.zh-CN.md)。

**Maven 依赖（`pom.xml`）：**

> 将 `最新版本号` 替换为上方 Maven Central 徽章中显示的版本。

```xml
<dependency>
  <groupId>com.manycoreapis</groupId>
  <artifactId>aholo-sdk-asset</artifactId>
  <version>最新版本号</version>
</dependency>
<dependency>
  <groupId>com.manycoreapis</groupId>
  <artifactId>aholo-sdk-world</artifactId>
  <version>最新版本号</version>
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

AssetClient asset = AssetClient.create(AholoClientConfig.ofRegion("cn"));
WorldClient world = WorldClient.create(AholoClientConfig.ofRegion("cn"));

// 上传视频文件
UploadResult uploaded = asset.uploadFile(Paths.get("room.mp4"));

// 创建 3D 世界重建任务
WorldAsyncOperation created = world.reconstructions().create(
    ReconstructionCreateParams.builder()
        .name("我的场景")
        .addResource(uploaded.url(), "video")
        .taskQuality("normal")
        .scene("model")
        .build());

// 等待任务完成
WorldDetail result = world.waitFor(created.worldId());
System.out.println(result);
```

### 世界任务输入资源

| API | 资源 `type` | 格式说明 |
|-----|-------------|----------|
| `reconstructions.create` | `image` \| `video` \| `insv` | 图片：≥20 张；视频：`.mp4`/`.mov`；Insta360：`.insv` |
| `generations.create` | 仅 `image` | 最多 1 张图，可与 `prompt` 组合 |

生成任务附带参考图（使用 `image`，不可用 `video`/`insv`）：

```typescript
await world.generations.create({
  prompt: '现代简约客厅',
  resources: [{ url: uploaded.url, type: 'image' }],
});
```

```python
world.generations.create(
    prompt='现代简约客厅',
    resources=[{'url': uploaded.url, 'type': 'image'}],
)
```

```java
import com.manycoreapis.sdk.world.model.GenerationCreateParams;
import com.manycoreapis.sdk.world.model.GenerateWorldResource;

world.generations().create(
    GenerationCreateParams.builder()
        .prompt("现代简约客厅")
        .addResource(GenerateWorldResource.of(uploaded.url()))
        .build());
```

## 错误处理

SDK 会抛出以下类型的异常：

| 异常类型 | 说明 |
|----------|------|
| `AuthenticationError` | API Key 无效或未设置 |
| `RateLimitError` | 请求频率超限 |
| `BusinessError` | 业务逻辑错误（含错误码） |
| `PollingTimeoutError` | 任务轮询超时 |
| `PollingFailedError` | 任务执行失败 |

```typescript
import { AuthenticationError, BusinessError } from '@manycore/aholo-sdk-core';

try {
  const result = await world.waitFor(worldId);
} catch (e) {
  if (e instanceof AuthenticationError) {
    console.error('API Key 无效');
  } else if (e instanceof BusinessError) {
    console.error('业务错误:', e.code, e.message);
  }
}
```

## 上传进度

资产上传支持进度回调：

```typescript
const uploaded = await asset.uploadFile('./room.mp4', {
  onProgress: (uploaded, total) => {
    const pct = Math.round((uploaded / total) * 100);
    process.stdout.write(`\r上传进度: ${pct}%`);
  },
});
```

## 示例代码

完整示例见各语言目录：

- TypeScript：[typescript/examples/](typescript/examples/)
- Python：[python/examples/](python/examples/)
- Java：[java/examples/](java/examples/)

## 许可证

[MIT](LICENSE)
