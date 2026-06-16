package com.manycoreapis.sdk.core;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public final class JsonSupport {
    public static final ObjectMapper MAPPER = new ObjectMapper()
            .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    public static final TypeReference<Map<String, Object>> MAP = new TypeReference<Map<String, Object>>() {};

    private JsonSupport() {}

    public static Map<String, Object> asMap(Object value) {
        return MAPPER.convertValue(value, MAP);
    }

    public static JsonNode readTree(String json) {
        try {
            return MAPPER.readTree(json);
        } catch (Exception e) {
            throw new AholoException("Failed to parse JSON", e);
        }
    }
}
