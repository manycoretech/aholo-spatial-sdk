package com.manycoreapis.sdk.core;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class OusHttpClient extends BaseHttpClient {
    public static final String OUS_TOKEN_HEADER = "ous-token-v2";

    public OusHttpClient(String baseUrl, String ousToken, int timeoutMs) {
        super(baseUrl.replaceAll("/$", ""), Duration.ofMillis(timeoutMs), singletonHeader(OUS_TOKEN_HEADER, ousToken));
    }

    private static Map<String, String> singletonHeader(String key, String value) {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(key, value);
        return Collections.unmodifiableMap(headers);
    }
}
