package com.manycoreapis.sdk.core;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class BaseHttpClient {
    protected final String baseUrl;
    protected final Duration timeout;
    protected final Map<String, String> defaultHeaders;
    protected final HttpClient httpClient;
    protected final ObjectMapper mapper = JsonSupport.MAPPER;

    public BaseHttpClient(String baseUrl, Duration timeout, Map<String, String> defaultHeaders) {
        this.baseUrl = baseUrl.replaceAll("/$", "");
        this.timeout = timeout;
        this.defaultHeaders = new LinkedHashMap<>(defaultHeaders);
        this.httpClient = HttpClient.newBuilder().connectTimeout(timeout).build();
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> request(
            String method,
            String path,
            Map<String, Object> query,
            Map<String, String> headers,
            Object jsonBody,
            MultipartBody multipart
    ) {
        try {
            String url = buildUrl(path, query);
            HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(url)).timeout(timeout);
            for (Map.Entry<String, String> entry : defaultHeaders.entrySet()) {
                builder.header(entry.getKey(), entry.getValue());
            }
            if (headers != null) {
                headers.forEach((k, v) -> { if (v != null) builder.header(k, v); });
            }

            if (multipart != null) {
                builder.header("Content-Type", multipart.contentType());
                builder.method(method, HttpRequest.BodyPublishers.ofByteArray(multipart.body()));
            } else if (jsonBody != null) {
                byte[] bytes = mapper.writeValueAsBytes(jsonBody);
                if (!hasContentType(builder)) {
                    builder.header("Content-Type", "application/json");
                }
                builder.method(method, HttpRequest.BodyPublishers.ofByteArray(bytes));
            } else {
                builder.method(method, HttpRequest.BodyPublishers.noBody());
            }

            HttpResponse<String> response = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
            Object parsed = parseBody(response.body());
            if (response.statusCode() >= 400) {
                ErrorSupport.throwForHttpStatus(response.statusCode(), parsed, method + " " + path);
            }
            if (parsed instanceof Map<?, ?> map) {
                return (Map<String, Object>) map;
            }
            return Map.of("value", parsed);
        } catch (AholoException e) {
            throw e;
        } catch (Exception e) {
            throw new AholoException("HTTP request failed: " + method + " " + path, e);
        }
    }

    private static boolean hasContentType(HttpRequest.Builder builder) {
        return false;
    }

    private String buildUrl(String path, Map<String, Object> query) {
        String normalized = path.startsWith("/") ? path : "/" + path;
        String url = baseUrl + normalized;
        if (query == null || query.isEmpty()) return url;
        String qs = query.entrySet().stream()
                .filter(e -> e.getValue() != null)
                .map(e -> URLEncoder.encode(e.getKey(), StandardCharsets.UTF_8) + "=" + URLEncoder.encode(String.valueOf(e.getValue()), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));
        return qs.isEmpty() ? url : url + "?" + qs;
    }

    private Object parseBody(String body) throws IOException {
        if (body == null || body.isBlank()) return Map.of();
        try {
            return mapper.readValue(body, Object.class);
        } catch (Exception ignored) {
            return body;
        }
    }
}
