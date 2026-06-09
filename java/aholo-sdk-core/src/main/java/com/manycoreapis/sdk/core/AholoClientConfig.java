package com.manycoreapis.sdk.core;

public record AholoClientConfig(
        String apiKey,
        String baseUrl,
        Region region,
        int timeoutMs,
        String userAgent
) {
    public enum Region { CN, COM }

    public static final String DEFAULT_BASE_URL_CN = "https://api.aholo3d.cn";
    public static final String DEFAULT_BASE_URL_GLOBAL = "https://api.aholo3d.com";
    public static final int DEFAULT_TIMEOUT_MS = 60_000;

    public AholoClientConfig {
        if (region == null) region = Region.CN;
        if (timeoutMs <= 0) timeoutMs = DEFAULT_TIMEOUT_MS;
    }

    /** API key resolved from the {@code AHOLO_API_KEY} environment variable; region defaults to {@code cn}. */
    public static AholoClientConfig defaults() {
        return new AholoClientConfig(null, null, Region.CN, DEFAULT_TIMEOUT_MS, null);
    }

    /**
     * Convenience factory: API key, region defaults to {@code cn}.
     *
     * <pre>{@code
     * AholoClientConfig config = AholoClientConfig.of("your_api_key");
     * }</pre>
     */
    public static AholoClientConfig of(String apiKey) {
        return new AholoClientConfig(apiKey, null, Region.CN, DEFAULT_TIMEOUT_MS, null);
    }

    /**
     * Convenience factory: API key + region ({@code "com"} or {@code "cn"}).
     *
     * <pre>{@code
     * AholoClientConfig config = AholoClientConfig.of("your_api_key", "com");
     * }</pre>
     */
    public static AholoClientConfig of(String apiKey, String region) {
        Region r = "com".equalsIgnoreCase(region) ? Region.COM : Region.CN;
        return new AholoClientConfig(apiKey, null, r, DEFAULT_TIMEOUT_MS, null);
    }

    /**
     * Convenience factory: region only; API key resolved from the {@code AHOLO_API_KEY} environment variable.
     *
     * <pre>{@code
     * AholoClientConfig config = AholoClientConfig.ofRegion("com");
     * }</pre>
     */
    public static AholoClientConfig ofRegion(String region) {
        Region r = "com".equalsIgnoreCase(region) ? Region.COM : Region.CN;
        return new AholoClientConfig(null, null, r, DEFAULT_TIMEOUT_MS, null);
    }

    public static String resolveBaseUrl(AholoClientConfig config) {
        if (config.baseUrl() != null && !config.baseUrl().isBlank()) {
            return config.baseUrl().replaceAll("/$", "");
        }
        return config.region() == Region.COM ? DEFAULT_BASE_URL_GLOBAL : DEFAULT_BASE_URL_CN;
    }

    public static String resolveApiKey(AholoClientConfig config) {
        String key = config.apiKey();
        if (key == null || key.isBlank()) {
            key = System.getenv("AHOLO_API_KEY");
        }
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException("Missing API key: set apiKey or AHOLO_API_KEY env var.");
        }
        return key;
    }
}
