package com.manycoreapis.sdk.core;

import java.util.Objects;

public class AholoClientConfig {
    private final String apiKey;
    private final String baseUrl;
    private final Region region;
    private final int timeoutMs;
    private final String userAgent;

    public enum Region { CN, COM }

    public static final String DEFAULT_BASE_URL_CN = "https://api.aholo3d.cn";
    public static final String DEFAULT_BASE_URL_GLOBAL = "https://api.aholo3d.com";
    public static final int DEFAULT_TIMEOUT_MS = 60_000;

    public AholoClientConfig(String apiKey, String baseUrl, Region region, int timeoutMs, String userAgent) {
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
        this.region = region == null ? Region.CN : region;
        this.timeoutMs = timeoutMs <= 0 ? DEFAULT_TIMEOUT_MS : timeoutMs;
        this.userAgent = userAgent;
    }

    public String apiKey() { return apiKey; }
    public String baseUrl() { return baseUrl; }
    public Region region() { return region; }
    public int timeoutMs() { return timeoutMs; }
    public String userAgent() { return userAgent; }

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
        if (config.baseUrl() != null && !EncodingSupport.isBlank(config.baseUrl())) {
            return config.baseUrl().replaceAll("/$", "");
        }
        return config.region() == Region.COM ? DEFAULT_BASE_URL_GLOBAL : DEFAULT_BASE_URL_CN;
    }

    public static String resolveApiKey(AholoClientConfig config) {
        String key = config.apiKey();
        if (EncodingSupport.isBlank(key)) {
            key = System.getenv("AHOLO_API_KEY");
        }
        if (EncodingSupport.isBlank(key)) {
            throw new IllegalArgumentException("Missing API key: set apiKey or AHOLO_API_KEY env var.");
        }
        return key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AholoClientConfig)) return false;
        AholoClientConfig that = (AholoClientConfig) o;
        return timeoutMs == that.timeoutMs
                && Objects.equals(apiKey, that.apiKey)
                && Objects.equals(baseUrl, that.baseUrl)
                && region == that.region
                && Objects.equals(userAgent, that.userAgent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(apiKey, baseUrl, region, timeoutMs, userAgent);
    }

    @Override
    public String toString() {
        return "AholoClientConfig{apiKey=" + (apiKey == null ? "null" : "***")
                + ", baseUrl=" + baseUrl
                + ", region=" + region
                + ", timeoutMs=" + timeoutMs
                + ", userAgent=" + userAgent + "}";
    }
}
