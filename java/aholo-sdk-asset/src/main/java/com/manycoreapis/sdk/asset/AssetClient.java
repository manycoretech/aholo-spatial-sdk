package com.manycoreapis.sdk.asset;

import com.manycoreapis.sdk.core.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Aholo asset upload client (OUS single/block upload with polling).
 *
 * <p>Stainless-style entry point:
 * <pre>{@code
 * AssetClient asset = AssetClient.create(AholoClientConfig.ofRegion("com"));
 * UploadResult result = asset.uploadFile(Paths.get("room.mp4"), (uploaded, total) -> { ... });
 * }</pre>
 */
@SuppressWarnings("unchecked")
public class AssetClient {
    private static final int OUS_STATUS_SUCCESS = 5;
    private static final int DEFAULT_PART_CONCURRENCY = 2;

    /** Called after each uploaded block (or after single upload). Both values are in bytes. */
    @FunctionalInterface
    public interface UploadProgressListener {
        void onProgress(long uploaded, long total);
    }

    private final AholoClientConfig config;
    private final AholoGatewayClient gateway;

    public AssetClient(AholoClientConfig config) {
        this.config = config == null ? AholoClientConfig.defaults() : config;
        this.gateway = new AholoGatewayClient(this.config);
    }

    public static AssetClient create(AholoClientConfig config) {
        return new AssetClient(config);
    }

    public Map<String, Object> getUploadToken() {
        String path = config.region() == AholoClientConfig.Region.COM ? "/global/asset/v1/token" : "/asset/v1/token";
        return gateway.gatewayRequest("GET", path, null, null, null);
    }

    public UploadResult uploadBytes(byte[] data, String filename) throws Exception {
        return uploadBytes(data, filename, null);
    }

    public UploadResult uploadBytes(byte[] data, String filename, UploadProgressListener onProgress) throws Exception {
        Map<String, Object> token = getUploadToken();
        OusHttpClient ous = new OusHttpClient(String.valueOf(token.get("globalDomain")), String.valueOf(token.get("ousToken")), config.timeoutMs());
        String md5 = md5Hex(data);
        int blockSize = ((Number) token.get("blockSize")).intValue();
        if (data.length <= blockSize) {
            singleUpload(ous, data, md5, filename, onProgress);
        } else {
            blockUpload(ous, data, md5, filename, blockSize, onProgress);
        }
        return pollStatus(ous);
    }

    public UploadResult uploadFile(Path filePath) throws Exception {
        return uploadBytes(Files.readAllBytes(filePath), filePath.getFileName().toString());
    }

    public UploadResult uploadFile(Path filePath, UploadProgressListener onProgress) throws Exception {
        return uploadBytes(Files.readAllBytes(filePath), filePath.getFileName().toString(), onProgress);
    }

    private void singleUpload(OusHttpClient ous, byte[] data, String md5, String filename, UploadProgressListener onProgress) {
        MultipartBody body = MultipartBody.builder()
                .textField("md5", md5)
                .fileField("file", filename, data, "application/octet-stream")
                .build();
        Map<String, Object> response = ous.request("POST", "/ous/api/v2/single/upload", null, null, null, body);
        CmdSupport.assertCmdOk(response, "single upload");
        if (onProgress != null) onProgress.onProgress(data.length, data.length);
    }

    private void blockUpload(OusHttpClient ous, byte[] data, String md5, String filename, int blockSize, UploadProgressListener onProgress) throws Exception {
        List<byte[]> parts = new ArrayList<>();
        for (int i = 0; i < data.length; i += blockSize) {
            int end = Math.min(i + blockSize, data.length);
            byte[] chunk = new byte[end - i];
            System.arraycopy(data, i, chunk, 0, chunk.length);
            parts.add(chunk);
        }
        Map<String, Object> query = new HashMap<>();
        query.put("md5", md5);
        query.put("blocks", parts.size());
        query.put("size", data.length);
        query.put("name", filename);
        Map<String, Object> initBody = ous.request("POST", "/ous/api/v2/block/upload/init", query, null, null, null);
        Map<String, Object> initData = (Map<String, Object>) CmdSupport.assertCmdSuccess(initBody, "block upload init");
        if (Boolean.TRUE.equals(initData.get("deduplicated"))) {
            return;
        }
        AtomicLong uploadedBytes = new AtomicLong(0);
        long totalBytes = data.length;
        ExecutorService pool = Executors.newFixedThreadPool(DEFAULT_PART_CONCURRENCY);
        try {
            List<Future<?>> futures = new ArrayList<>();
            for (int i = 0; i < parts.size(); i++) {
                final int index = i;
                futures.add(pool.submit(() -> {
                    MultipartBody partBody = MultipartBody.builder()
                            .textField("block", String.valueOf(index + 1))
                            .fileField("file", filename + ".part" + (index + 1), parts.get(index), "application/octet-stream")
                            .build();
                    Map<String, Object> response = ous.request("POST", "/ous/api/v2/block/upload/part", null, null, null, partBody);
                    CmdSupport.assertCmdOk(response, "block upload part " + (index + 1));
                    if (onProgress != null) {
                        onProgress.onProgress(uploadedBytes.addAndGet(parts.get(index).length), totalBytes);
                    }
                    return null;
                }));
            }
            for (Future<?> future : futures) future.get();
        } finally {
            pool.shutdown();
        }
    }

    private UploadResult pollStatus(OusHttpClient ous) throws Exception {
        Map<String, Object> status = PollSupport.pollUntil(
                () -> (Map<String, Object>) CmdSupport.assertCmdSuccess(
                        ous.request("GET", "/ous/api/v2/upload/status", null, null, null, null),
                        "upload status"
                ),
                d -> OUS_STATUS_SUCCESS == ((Number) d.get("status")).intValue() && d.get("url") != null,
                d -> {
                    Number s = (Number) d.get("status");
                    return s != null && (s.intValue() == 6 || s.intValue() == 8);
                },
                d -> "Upload failed status=" + d.get("status"),
                Duration.ofMillis(300),
                Duration.ofMinutes(5)
        );
        return new UploadResult(
                String.valueOf(status.get("url")),
                status.get("md5") == null ? "" : String.valueOf(status.get("md5")),
                status.get("uploadKey") == null ? null : String.valueOf(status.get("uploadKey")),
                status.get("obsTaskId") == null ? null : String.valueOf(status.get("obsTaskId"))
        );
    }

    private static String md5Hex(byte[] data) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] hash = digest.digest(data);
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
