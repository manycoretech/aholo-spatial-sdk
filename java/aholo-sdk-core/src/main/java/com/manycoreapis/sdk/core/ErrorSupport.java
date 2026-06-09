package com.manycoreapis.sdk.core;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;

public final class ErrorSupport {
    private ErrorSupport() {}

    @SuppressWarnings("unchecked")
    public static void throwForHttpStatus(int statusCode, Object body, String context) {
        if (body instanceof Map<?, ?> map && map.containsKey("c")) {
            CmdSupport.assertCmdSuccess((Map<String, Object>) map, context);
        }
        if (body instanceof Map<?, ?> map && map.containsKey("status") && map.containsKey("message")) {
            String message = String.valueOf(map.get("message"));
            String status = map.get("status") == null ? null : String.valueOf(map.get("status"));
            String bizCode = null;
            Object details = map.get("details");
            if (details instanceof Map<?, ?> dmap) {
                Object meta = dmap.get("metaData");
                if (meta instanceof Map<?, ?> metaMap && metaMap.get("bizCode") != null) {
                    bizCode = String.valueOf(metaMap.get("bizCode"));
                }
            }
            if (statusCode == 401 || "UNAUTHENTICATED".equals(status)) {
                throw new AuthenticationException(message, statusCode, status, bizCode, body);
            }
            if (statusCode == 429 || "RESOURCE_EXHAUSTED".equals(status)) {
                throw new RateLimitException(message, statusCode, status, bizCode, body);
            }
            throw new AholoException(message, statusCode, status, bizCode, body);
        }
        throw new AholoException("HTTP " + statusCode + " for " + context, statusCode, null, null, body);
    }
}
