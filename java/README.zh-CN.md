# Aholo Spatial SDK — Java

[English](README.md)

[Aholo](https://labs.aholo3d.cn) OpenAPI 官方 Java SDK。

> 📖 **完整文档（canonical）**：[https://labs.aholo3d.cn/api-docs/sdk/java](https://labs.aholo3d.cn/api-docs/sdk/java)

**运行要求：** Java ≥ 17，Maven ≥ 3.6

## 包列表

| Artifact | 版本 | 说明 |
|----------|------|------|
| `com.manycoreapis:aholo-sdk-asset` | [![Maven Central](https://img.shields.io/maven-central/v/com.manycoreapis/aholo-sdk-asset?color=C71A36&logo=apachemaven&logoColor=white)](https://central.sonatype.com/artifact/com.manycoreapis/aholo-sdk-asset) | 文件上传（单文件/分块上传，支持断点续传） |
| `com.manycoreapis:aholo-sdk-lux3d` | [![Maven Central](https://img.shields.io/maven-central/v/com.manycoreapis/aholo-sdk-lux3d?color=C71A36&logo=apachemaven&logoColor=white)](https://central.sonatype.com/artifact/com.manycoreapis/aholo-sdk-lux3d) | Lux3D 图像/文字转 3D 生成、材质迁移 |
| `com.manycoreapis:aholo-sdk-world` | [![Maven Central](https://img.shields.io/maven-central/v/com.manycoreapis/aholo-sdk-world?color=C71A36&logo=apachemaven&logoColor=white)](https://central.sonatype.com/artifact/com.manycoreapis/aholo-sdk-world) | 3DGS 世界重建与生成 |
| `com.manycoreapis:aholo-sdk-core` | [![Maven Central](https://img.shields.io/maven-central/v/com.manycoreapis/aholo-sdk-core?color=C71A36&logo=apachemaven&logoColor=white)](https://central.sonatype.com/artifact/com.manycoreapis/aholo-sdk-core) | 公共 HTTP 客户端、鉴权、错误类型、轮询 |

## 安装

在 `pom.xml` 中添加所需依赖（将 `最新版本号` 替换为上方 Maven Central 徽章显示的版本）：

```xml
<dependency>
  <groupId>com.manycoreapis</groupId>
  <artifactId>aholo-sdk-asset</artifactId>
  <version>最新版本号</version>
</dependency>

<dependency>
  <groupId>com.manycoreapis</groupId>
  <artifactId>aholo-sdk-lux3d</artifactId>
  <version>最新版本号</version>
</dependency>

<dependency>
  <groupId>com.manycoreapis</groupId>
  <artifactId>aholo-sdk-world</artifactId>
  <version>最新版本号</version>
</dependency>
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

> API 用法与错误处理见 [完整文档](https://labs.aholo3d.cn/api-docs/sdk/java)。

## 示例代码

见 [examples/](examples/) 目录，运行方式见[完整文档](https://labs.aholo3d.cn/api-docs/sdk/java#示例代码)。

## 许可证

[MIT](../LICENSE)
