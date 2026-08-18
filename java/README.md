# Aholo Spatial SDK — Java

[中文文档](README.zh-CN.md)

Official Java SDKs for the [Aholo](https://labs.aholo3d.com) Open API.

> 📖 **Full documentation (canonical)**: [https://labs.aholo3d.com/api-docs/en/sdk/java](https://labs.aholo3d.com/api-docs/en/sdk/java)

**Requirements:** Java 8+, Maven ≥ 3.6 (build with JDK 17 or 21 recommended)

## Packages

| Artifact | Version | Description |
|----------|---------|-------------|
| `com.manycoreapis:aholo-sdk-asset` | [![Maven Central](https://img.shields.io/maven-central/v/com.manycoreapis/aholo-sdk-asset?color=C71A36&logo=apachemaven&logoColor=white)](https://central.sonatype.com/artifact/com.manycoreapis/aholo-sdk-asset) | File upload (single & multipart, resume support) |
| `com.manycoreapis:aholo-sdk-lux3d` | [![Maven Central](https://img.shields.io/maven-central/v/com.manycoreapis/aholo-sdk-lux3d?color=C71A36&logo=apachemaven&logoColor=white)](https://central.sonatype.com/artifact/com.manycoreapis/aholo-sdk-lux3d) | Lux3D generation, material transfer, part split, task history |
| `com.manycoreapis:aholo-sdk-world` | [![Maven Central](https://img.shields.io/maven-central/v/com.manycoreapis/aholo-sdk-world?color=C71A36&logo=apachemaven&logoColor=white)](https://central.sonatype.com/artifact/com.manycoreapis/aholo-sdk-world) | 3DGS world reconstruction & generation |
| `com.manycoreapis:aholo-sdk-core` | [![Maven Central](https://img.shields.io/maven-central/v/com.manycoreapis/aholo-sdk-core?color=C71A36&logo=apachemaven&logoColor=white)](https://central.sonatype.com/artifact/com.manycoreapis/aholo-sdk-core) | Shared HTTP client, auth, error types, polling |

## Installation

Add the packages you need to your `pom.xml` (replace `LATEST_VERSION` with the version shown in the Maven Central badge above):

```xml
<dependency>
  <groupId>com.manycoreapis</groupId>
  <artifactId>aholo-sdk-asset</artifactId>
  <version>LATEST_VERSION</version>
</dependency>

<dependency>
  <groupId>com.manycoreapis</groupId>
  <artifactId>aholo-sdk-lux3d</artifactId>
  <version>LATEST_VERSION</version>
</dependency>

<dependency>
  <groupId>com.manycoreapis</groupId>
  <artifactId>aholo-sdk-world</artifactId>
  <version>LATEST_VERSION</version>
</dependency>
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

> API usage and error handling: see [full documentation](https://labs.aholo3d.com/api-docs/en/sdk/java).

## Examples

See [examples/](examples/) for runnable Java classes. Run instructions are in the [full documentation](https://labs.aholo3d.com/api-docs/en/sdk/java).

- `WorldReconstruct` — `.mp4`/`.mov` (`video`) or Insta360 `.insv` (`insv`); generation uses `GenerateWorldResource`

## License

[MIT](../LICENSE)
