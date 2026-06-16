package com.manycoreapis.sdk.core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
        private final Map<String, Part> parts = new LinkedHashMap<String, Part>();

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
            try {
                for (Part part : parts.values()) {
                    writeBytes(out, ("--" + boundary + "\r\n").getBytes(StandardCharsets.UTF_8));
                    if (part.filename == null) {
                        writeBytes(out, ("Content-Disposition: form-data; name=\"" + part.name + "\"\r\n\r\n").getBytes(StandardCharsets.UTF_8));
                    } else {
                        writeBytes(out, ("Content-Disposition: form-data; name=\"" + part.name + "\"; filename=\"" + part.filename + "\"\r\n").getBytes(StandardCharsets.UTF_8));
                        writeBytes(out, ("Content-Type: " + part.mime + "\r\n\r\n").getBytes(StandardCharsets.UTF_8));
                    }
                    writeBytes(out, part.content);
                    writeBytes(out, "\r\n".getBytes(StandardCharsets.UTF_8));
                }
                writeBytes(out, ("--" + boundary + "--\r\n").getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                throw new IllegalStateException("Failed to build multipart body", e);
            }
            return new MultipartBody(out.toByteArray(), "multipart/form-data; boundary=" + boundary);
        }

        private static void writeBytes(ByteArrayOutputStream out, byte[] bytes) throws IOException {
            out.write(bytes, 0, bytes.length);
        }

        private static final class Part {
            private final String name;
            private final byte[] content;
            private final String filename;
            private final String mime;

            private Part(String name, byte[] content, String filename, String mime) {
                this.name = name;
                this.content = content;
                this.filename = filename;
                this.mime = mime;
            }
        }
    }
}
