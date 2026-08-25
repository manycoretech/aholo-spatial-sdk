package com.manycoreapis.sdk.lux3d.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/** Parameters for creating a material-transfer task. */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class MaterialTransferCreateParams {
    private static final List<String> OUTPUT_FORMATS =
            Arrays.asList("zip", "glb", "usdz", "obj_zip", "fbx_zip");

    @JsonProperty("img") private final String img;
    @JsonProperty("meshUrl") private final String meshUrl;
    @JsonProperty("version") private final String version;
    @JsonProperty("outputFormat") private final List<String> outputFormat;
    @JsonProperty("aiPredictSize") private final Boolean aiPredictSize;
    @JsonProperty("customSize") private final Float customSize;

    private MaterialTransferCreateParams(Builder b) {
        this.img = b.img;
        this.meshUrl = b.meshUrl;
        this.version = b.version;
        this.outputFormat = b.outputFormat;
        this.aiPredictSize = b.aiPredictSize;
        this.customSize = b.customSize;
    }

    public static Builder builder() { return new Builder(); }
    public String img() { return img; }
    public String meshUrl() { return meshUrl; }
    public String version() { return version; }
    public Optional<List<String>> outputFormat() { return Optional.ofNullable(outputFormat); }
    public Optional<Boolean> aiPredictSize() { return Optional.ofNullable(aiPredictSize); }
    public Optional<Float> customSize() { return Optional.ofNullable(customSize); }

    public static final class Builder {
        private String img;
        private String meshUrl;
        private String version;
        private List<String> outputFormat;
        private Boolean aiPredictSize;
        private Float customSize;

        private Builder() {}
        public Builder img(String img) { this.img = img; return this; }
        public Builder meshUrl(String meshUrl) { this.meshUrl = meshUrl; return this; }
        public Builder version(String version) { this.version = version; return this; }
        public Builder outputFormat(List<String> outputFormat) { this.outputFormat = outputFormat; return this; }
        public Builder aiPredictSize(Boolean aiPredictSize) { this.aiPredictSize = aiPredictSize; return this; }
        public Builder customSize(Float customSize) { this.customSize = customSize; return this; }

        public MaterialTransferCreateParams build() {
            Objects.requireNonNull(img, "img is required");
            Objects.requireNonNull(meshUrl, "meshUrl is required");
            Objects.requireNonNull(version, "version is required");
            if (img.trim().isEmpty()) throw new IllegalArgumentException("img must not be empty");
            if (meshUrl.trim().isEmpty()) throw new IllegalArgumentException("meshUrl must not be empty");
            if (!"v3.0-standard".equals(version)) {
                throw new IllegalArgumentException("version must be v3.0-standard");
            }
            if (outputFormat != null && !OUTPUT_FORMATS.containsAll(outputFormat)) {
                throw new IllegalArgumentException("unsupported material-transfer outputFormat");
            }
            if (customSize != null && customSize <= 0) {
                throw new IllegalArgumentException("customSize must be greater than 0");
            }
            return new MaterialTransferCreateParams(this);
        }
    }
}
