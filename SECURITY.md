# Security Policy

## Supported Versions

Security fixes are applied to the latest release. We recommend always using the newest SDK version.

| Version | Supported |
| ------- | --------- |
| 1.2.x   | ✅        |
| 1.1.x   | ❌        |
| < 1.1   | ❌        |

Packages covered: TypeScript (`@manycore/aholo-sdk-*`), Python (`manycore-aholo-sdk-*`), and Java (`com.manycoreapis:aholo-sdk-*`).

## Reporting a Vulnerability

**Please do not report security vulnerabilities through public GitHub issues.**

If you believe you have found a security issue in this SDK, report it privately using one of the following:

1. **GitHub Security Advisories** (preferred): [Report a vulnerability](https://github.com/manycoretech/aholo-spatial-sdk/security/advisories/new)
2. **Email**: devops-team@qunhemail.com (if the advisory form is unavailable)

Include as much detail as possible:

- Affected package(s) and version(s)
- Steps to reproduce
- Impact assessment (e.g. credential exposure, insecure defaults)
- Proof of concept, if available

## What to Expect

- **Acknowledgment** within 5 business days
- **Initial assessment** within 10 business days
- We will coordinate disclosure timing with you and publish a fix or mitigation before any public announcement when possible

## Scope

**In scope**

- Vulnerabilities in this repository's source code (HTTP client handling, credential storage patterns, dependency usage bundled by the SDK, etc.)

**Out of scope**

- Vulnerabilities in the Aholo API backend itself (report via [Aholo support](https://labs.aholo3d.com))
- Issues caused solely by misuse of API keys or insecure application code outside this SDK
- Social engineering or denial-of-service against Aholo infrastructure

## Safe Usage Reminders

- Never commit `AHOLO_API_KEY` or other secrets to source control.
- Prefer environment variables or your platform's secret manager.
- Rotate API keys promptly if you suspect exposure.

Thank you for helping keep Aholo Spatial SDK users safe.
