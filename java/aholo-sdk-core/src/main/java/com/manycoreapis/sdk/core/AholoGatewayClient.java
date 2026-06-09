package com.manycoreapis.sdk.core;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class AholoGatewayClient extends BaseHttpClient {
    public static final String DEFAULT_USER_AGENT = "aholo-sdk-core/1.1.0";
    private final AholoClientConfig config;

    public AholoGatewayClient(AholoClientConfig config) {
        super(
                AholoClientConfig.resolveBaseUrl(config),
                Duration.ofMillis(config.timeoutMs()),
                buildHeaders(config)
        );
        this.config = config;
    }

    private static Map<String, String> buildHeaders(AholoClientConfig config) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", AholoClientConfig.resolveApiKey(config));
        String ua = config.userAgent() == null ? DEFAULT_USER_AGENT : DEFAULT_USER_AGENT + " " + config.userAgent();
        headers.put("User-Agent", ua);
        return headers;
    }

    public Map<String, Object> gatewayRequest(String method, String path, Map<String, Object> query, Map<String, String> headers, Object body) {
        return request(method, path, query, headers, body, null);
    }

    /** Performs a gateway request and converts the raw map response into {@code responseType}. */
    public <T> T gatewayRequest(String method, String path, Map<String, Object> query, Map<String, String> headers, Object body, Class<T> responseType) {
        Map<String, Object> raw = gatewayRequest(method, path, query, headers, body);
        return JsonSupport.MAPPER.convertValue(raw, responseType);
    }

    public AholoClientConfig config() { return config; }
}
