package com.manycoreapis.sdk.lux3d.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Optional;

/**
 * Parameters for creating an image-to-3D task.
 * Provide exactly one of {@code img} (single image) or {@code imgs} (G1 multi-view)
 * when calling {@code create}; either may be omitted for option-only templates used by
 * {@code createFromFile(s)}.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ImgTo3dCreateParams {

    @JsonProperty("img")
    private final String img;

    @JsonProperty("imgs")
    private final List<String> imgs;

    @JsonProperty("version")
    private final String version;

    @JsonProperty("faceCount")
    private final Integer faceCount;

    @JsonProperty("outputFormat")
    private final List<String> outputFormat;

    @JsonProperty("enablePbr")
    private final Boolean enablePbr;

    @JsonProperty("textureSize")
    private final Integer textureSize;

    private ImgTo3dCreateParams(Builder b) {
        this.img = b.img;
        this.imgs = b.imgs;
        this.version = b.version;
        this.faceCount = b.faceCount;
        this.outputFormat = b.outputFormat;
        this.enablePbr = b.enablePbr;
        this.textureSize = b.textureSize;
    }

    public static Builder builder() { return new Builder(); }

    public Optional<String> img()                { return Optional.ofNullable(img); }
    public Optional<List<String>> imgs()         { return Optional.ofNullable(imgs); }
    public Optional<String> version()            { return Optional.ofNullable(version); }
    public Optional<Integer> faceCount()         { return Optional.ofNullable(faceCount); }
    public Optional<List<String>> outputFormat() { return Optional.ofNullable(outputFormat); }
    public Optional<Boolean> enablePbr()         { return Optional.ofNullable(enablePbr); }
    public Optional<Integer> textureSize()       { return Optional.ofNullable(textureSize); }

    public static final class Builder {
        private String img;
        private List<String> imgs;
        private String version;
        private Integer faceCount;
        private List<String> outputFormat;
        private Boolean enablePbr;
        private Integer textureSize;

        private Builder() {}

        /** Base64 data-URL or remote URL of the input image (single-image path). */
        public Builder img(String img)                       { this.img = img; return this; }
        /** G1 multi-view images (URL or data-URL). Mutually exclusive with {@link #img}. */
        public Builder imgs(List<String> imgs)               { this.imgs = imgs; return this; }
        /**
         * Model version: {@code "v3.0-standard"} (default), {@code "v2.0-preview"},
         * {@code "v1.0-pro"}, or {@code "G1"}.
         */
        public Builder version(String version)               { this.version = version; return this; }
        /** Target face count for v2 / v3 / G1 (10_000–500_000). */
        public Builder faceCount(Integer faceCount)          { this.faceCount = faceCount; return this; }
        /**
         * Output formats. v2/v3: {@code zip}, {@code glb}, {@code usdz}, {@code obj_zip}, {@code fbx_zip}.
         * G1: {@code zip}, {@code glb}, {@code ply}.
         */
        public Builder outputFormat(List<String> outputFormat){ this.outputFormat = outputFormat; return this; }
        /** G1 only: generate textured/PBR mesh (default true). */
        public Builder enablePbr(Boolean enablePbr)          { this.enablePbr = enablePbr; return this; }
        /** G1 only: texture size when enablePbr is true (default 2048). */
        public Builder textureSize(Integer textureSize)      { this.textureSize = textureSize; return this; }

        public ImgTo3dCreateParams build() {
            boolean hasImg = img != null;
            boolean hasImgs = imgs != null;
            // Allow neither so callers can pass option-only templates to createFromFile(s).
            if (hasImg && hasImgs) {
                throw new IllegalArgumentException("img and imgs are mutually exclusive");
            }
            if (hasImgs && imgs.isEmpty()) {
                throw new IllegalArgumentException("imgs must not be empty");
            }
            return new ImgTo3dCreateParams(this);
        }
    }
}
