package com.manycoreapis.sdk.asset;

import java.util.Objects;

public class UploadResult {
    private final String url;
    private final String md5;
    private final String uploadKey;
    private final String obsTaskId;

    public UploadResult(String url, String md5, String uploadKey, String obsTaskId) {
        this.url = url;
        this.md5 = md5;
        this.uploadKey = uploadKey;
        this.obsTaskId = obsTaskId;
    }

    public String url() { return url; }
    public String md5() { return md5; }
    public String uploadKey() { return uploadKey; }
    public String obsTaskId() { return obsTaskId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UploadResult)) return false;
        UploadResult that = (UploadResult) o;
        return Objects.equals(url, that.url)
                && Objects.equals(md5, that.md5)
                && Objects.equals(uploadKey, that.uploadKey)
                && Objects.equals(obsTaskId, that.obsTaskId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, md5, uploadKey, obsTaskId);
    }

    @Override
    public String toString() {
        return "UploadResult{url='" + url + "', md5='" + md5
                + "', uploadKey='" + uploadKey + "', obsTaskId='" + obsTaskId + "'}";
    }
}
