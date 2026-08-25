package com.manycoreapis.sdk.lux3d.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

/** Parameters for creating a multi-format export task. */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class MultiFormatExportCreateParams {
    private static final List<String> OUTPUT_FORMATS = Arrays.asList("usdz", "obj_zip", "fbx_zip");
    @JsonProperty("modelUrl") private final String modelUrl;
    @JsonProperty("outputFormat") private final List<String> outputFormat;

    private MultiFormatExportCreateParams(Builder builder) {
        this.modelUrl = builder.modelUrl;
        this.outputFormat = builder.outputFormat;
    }
    public static Builder builder() { return new Builder(); }
    public String modelUrl() { return modelUrl; }
    public Optional<List<String>> outputFormat() { return Optional.ofNullable(outputFormat); }

    public static final class Builder {
        private String modelUrl;
        private List<String> outputFormat;
        private Builder() {}
        public Builder modelUrl(String modelUrl) { this.modelUrl = modelUrl; return this; }
        public Builder outputFormat(List<String> outputFormat) { this.outputFormat = outputFormat; return this; }
        public MultiFormatExportCreateParams build() {
            Objects.requireNonNull(modelUrl, "modelUrl is required");
            if (modelUrl.trim().isEmpty()) throw new IllegalArgumentException("modelUrl must not be empty");
            if (outputFormat != null && !OUTPUT_FORMATS.containsAll(outputFormat)) {
                throw new IllegalArgumentException("outputFormat supports usdz, obj_zip, and fbx_zip");
            }
            String path = modelUrl.toLowerCase(Locale.ROOT).split("[?#]", 2)[0];
            if (path.endsWith(".glb") && (outputFormat == null || outputFormat.isEmpty())) {
                throw new IllegalArgumentException("outputFormat is required for GLB input");
            }
            return new MultiFormatExportCreateParams(this);
        }
    }
}
