package com.manycoreapis.sdk.lux3d.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/** Parameters for creating an image-to-3D task. */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ImgTo3dCreateParams {
    private static final List<String> VERSIONS = Arrays.asList("G1", "G1-Turbo");
    private static final List<String> OUTPUT_FORMATS = Arrays.asList("zip", "glb", "ply");

    @JsonProperty("img") private final String img;
    @JsonProperty("imgs") private final List<String> imgs;
    @JsonProperty("version") private final String version;
    @JsonProperty("faceCount") private final Integer faceCount;
    @JsonProperty("outputFormat") private final List<String> outputFormat;
    @JsonProperty("enablePbr") private final Boolean enablePbr;
    @JsonProperty("aiPredictSize") private final Boolean aiPredictSize;

    private ImgTo3dCreateParams(Builder b) {
        this.img = b.img;
        this.imgs = b.imgs;
        this.version = b.version;
        this.faceCount = b.faceCount;
        this.outputFormat = b.outputFormat;
        this.enablePbr = b.enablePbr;
        this.aiPredictSize = b.aiPredictSize;
    }

    public static Builder builder() { return new Builder(); }

    public Optional<String> img() { return Optional.ofNullable(img); }
    public Optional<List<String>> imgs() { return Optional.ofNullable(imgs); }
    public String version() { return version; }
    public Optional<Integer> faceCount() { return Optional.ofNullable(faceCount); }
    public Optional<List<String>> outputFormat() { return Optional.ofNullable(outputFormat); }
    public Optional<Boolean> enablePbr() { return Optional.ofNullable(enablePbr); }
    public Optional<Boolean> aiPredictSize() { return Optional.ofNullable(aiPredictSize); }

    public static final class Builder {
        private String img;
        private List<String> imgs;
        private String version;
        private Integer faceCount;
        private List<String> outputFormat;
        private Boolean enablePbr;
        private Boolean aiPredictSize;

        private Builder() {}

        public Builder img(String img) { this.img = img; return this; }
        public Builder imgs(List<String> imgs) { this.imgs = imgs; return this; }
        public Builder version(String version) { this.version = version; return this; }
        public Builder faceCount(Integer faceCount) { this.faceCount = faceCount; return this; }
        public Builder outputFormat(List<String> outputFormat) { this.outputFormat = outputFormat; return this; }
        public Builder enablePbr(Boolean enablePbr) { this.enablePbr = enablePbr; return this; }
        public Builder aiPredictSize(Boolean aiPredictSize) { this.aiPredictSize = aiPredictSize; return this; }

        public ImgTo3dCreateParams build() {
            Objects.requireNonNull(version, "version is required");
            if (!VERSIONS.contains(version)) throw new IllegalArgumentException("version must be G1 or G1-Turbo");
            boolean hasImg = img != null;
            boolean hasImgs = imgs != null;
            if (hasImg && hasImgs) throw new IllegalArgumentException("img and imgs are mutually exclusive");
            if (hasImg && img.trim().isEmpty()) throw new IllegalArgumentException("img must not be empty");
            if (hasImgs && (imgs.isEmpty() || imgs.size() > 32)) {
                throw new IllegalArgumentException("imgs must contain between 1 and 32 images");
            }
            if (faceCount != null && (faceCount < 10_000 || faceCount > 300_000)) {
                throw new IllegalArgumentException("faceCount must be between 10000 and 300000");
            }
            if (outputFormat != null && !OUTPUT_FORMATS.containsAll(outputFormat)) {
                throw new IllegalArgumentException("outputFormat supports zip, glb, and ply");
            }
            return new ImgTo3dCreateParams(this);
        }
    }
}
