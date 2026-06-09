package com.manycoreapis.sdk.core;

import java.time.Duration;
import java.util.Map;

public class OusHttpClient extends BaseHttpClient {
    public static final String OUS_TOKEN_HEADER = "ous-token-v2";

    public OusHttpClient(String baseUrl, String ousToken, int timeoutMs) {
        super(baseUrl.replaceAll("/$", ""), Duration.ofMillis(timeoutMs), Map.of(OUS_TOKEN_HEADER, ousToken));
    }
}
