package com.manycoreapis.sdk.core;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public final class MultipartBody {
    private final byte[] body;
    private final String contentType;

    private MultipartBody(byte[] body, String contentType) {
        this.body = body;
        this.contentType = contentType;
    }

    public byte[] body() { return body; }
    public String contentType() { return contentType; }

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private final String boundary = "----AholoSdk" + UUID.randomUUID();
        private final Map<String, Part> parts = new LinkedHashMap<>();

        public Builder textField(String name, String value) {
            parts.put(name, new Part(name, value.getBytes(StandardCharsets.UTF_8), null, "text/plain"));
            return this;
        }

        public Builder fileField(String name, String filename, byte[] content, String mime) {
            parts.put(name, new Part(name, content, filename, mime));
            return this;
        }

        public MultipartBody build() {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            for (Part part : parts.values()) {
                out.writeBytes(("--" + boundary + "\r\n").getBytes(StandardCharsets.UTF_8));
                if (part.filename == null) {
                    out.writeBytes(("Content-Disposition: form-data; name=\"" + part.name + "\"\r\n\r\n").getBytes(StandardCharsets.UTF_8));
                } else {
                    out.writeBytes(("Content-Disposition: form-data; name=\"" + part.name + "\"; filename=\"" + part.filename + "\"\r\n").getBytes(StandardCharsets.UTF_8));
                    out.writeBytes(("Content-Type: " + part.mime + "\r\n\r\n").getBytes(StandardCharsets.UTF_8));
                }
                out.writeBytes(part.content);
                out.writeBytes("\r\n".getBytes(StandardCharsets.UTF_8));
            }
            out.writeBytes(("--" + boundary + "--\r\n").getBytes(StandardCharsets.UTF_8));
            return new MultipartBody(out.toByteArray(), "multipart/form-data; boundary=" + boundary);
        }

        private record Part(String name, byte[] content, String filename, String mime) {}
    }
}
