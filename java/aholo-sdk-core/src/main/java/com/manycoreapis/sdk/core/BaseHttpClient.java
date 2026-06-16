package com.manycoreapis.sdk.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class BaseHttpClient {
    protected final String baseUrl;
    protected final Duration timeout;
    protected final Map<String, String> defaultHeaders;
    protected final OkHttpClient httpClient;
    protected final ObjectMapper mapper = JsonSupport.MAPPER;

    public BaseHttpClient(String baseUrl, Duration timeout, Map<String, String> defaultHeaders) {
        this.baseUrl = baseUrl.replaceAll("/$", "");
        this.timeout = timeout;
        this.defaultHeaders = new LinkedHashMap<String, String>(defaultHeaders);
        long timeoutMs = timeout.toMillis();
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(timeoutMs, TimeUnit.MILLISECONDS)
                .readTimeout(timeoutMs, TimeUnit.MILLISECONDS)
                .writeTimeout(timeoutMs, TimeUnit.MILLISECONDS)
                .callTimeout(timeoutMs, TimeUnit.MILLISECONDS)
                .build();
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
            Request.Builder builder = new Request.Builder().url(url);
            for (Map.Entry<String, String> entry : defaultHeaders.entrySet()) {
                builder.header(entry.getKey(), entry.getValue());
            }
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    if (entry.getValue() != null) {
                        builder.header(entry.getKey(), entry.getValue());
                    }
                }
            }

            RequestBody requestBody = null;
            if (multipart != null) {
                MediaType mediaType = MediaType.parse(multipart.contentType());
                requestBody = RequestBody.create(multipart.body(), mediaType);
            } else if (jsonBody != null) {
                byte[] bytes = mapper.writeValueAsBytes(jsonBody);
                requestBody = RequestBody.create(bytes, MediaType.parse("application/json"));
            }

            builder.method(method, requestBody);

            try (Response response = httpClient.newCall(builder.build()).execute()) {
                String responseBody = response.body() != null ? response.body().string() : "";
                Object parsed = parseBody(responseBody);
                int statusCode = response.code();
                if (statusCode >= 400) {
                    ErrorSupport.throwForHttpStatus(statusCode, parsed, method + " " + path);
                }
                if (parsed instanceof Map) {
                    return (Map<String, Object>) parsed;
                }
                Map<String, Object> result = new HashMap<String, Object>();
                result.put("value", parsed);
                return result;
            }
        } catch (AholoException e) {
            throw e;
        } catch (Exception e) {
            throw new AholoException("HTTP request failed: " + method + " " + path, e);
        }
    }

    private String buildUrl(String path, Map<String, Object> query) {
        String normalized = path.startsWith("/") ? path : "/" + path;
        String url = baseUrl + normalized;
        if (query == null || query.isEmpty()) return url;
        String qs = query.entrySet().stream()
                .filter(e -> e.getValue() != null)
                .map(e -> EncodingSupport.urlEncode(e.getKey()) + "=" + EncodingSupport.urlEncode(String.valueOf(e.getValue())))
                .collect(Collectors.joining("&"));
        return qs.isEmpty() ? url : url + "?" + qs;
    }

    private Object parseBody(String body) throws IOException {
        if (body == null || EncodingSupport.isBlank(body)) {
            return new HashMap<String, Object>();
        }
        try {
            return mapper.readValue(body, Object.class);
        } catch (Exception ignored) {
            return body;
        }
    }

}
