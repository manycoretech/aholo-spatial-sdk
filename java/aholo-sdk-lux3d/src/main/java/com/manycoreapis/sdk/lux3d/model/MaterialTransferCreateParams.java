package com.manycoreapis.sdk.lux3d.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/** Parameters for creating a material-transfer task. G1 is not supported. */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class MaterialTransferCreateParams {

    @JsonProperty("img")
    private final String img;

    @JsonProperty("meshUrl")
    private final String meshUrl;

    @JsonProperty("version")
    private final String version;

    @JsonProperty("outputFormat")
    private final List<String> outputFormat;

    private MaterialTransferCreateParams(Builder b) {
        this.img = b.img;
        this.meshUrl = b.meshUrl;
        this.version = b.version;
        this.outputFormat = b.outputFormat;
    }

    public static Builder builder() { return new Builder(); }

    public String img()                          { return img; }
    public String meshUrl()                      { return meshUrl; }
    public Optional<String> version()            { return Optional.ofNullable(version); }
    public Optional<List<String>> outputFormat() { return Optional.ofNullable(outputFormat); }

    public static final class Builder {
        private String img;
        private String meshUrl;
        private String version;
        private List<String> outputFormat;

        private Builder() {}

        /** Reference material image (base64 data-URL or remote URL). */
        public Builder img(String img)             { this.img = img; return this; }
        /** URL of the target mesh to apply the material to. */
        public Builder meshUrl(String meshUrl)     { this.meshUrl = meshUrl; return this; }
        /**
         * Model version: {@code "v3.0-standard"} (default), {@code "v2.0-preview"}, or {@code "v1.0-pro"}.
         * G1 is not supported for material transfer.
         */
        public Builder version(String version)     { this.version = version; return this; }
        /**
         * Output formats. v1: {@code zip} only; v2/v3: {@code zip}, {@code glb}, {@code usdz},
         * {@code obj_zip}, {@code fbx_zip}.
         */
        public Builder outputFormat(List<String> outputFormat){ this.outputFormat = outputFormat; return this; }

        public MaterialTransferCreateParams build() {
            Objects.requireNonNull(img,     "img is required");
            Objects.requireNonNull(meshUrl, "meshUrl is required");
            return new MaterialTransferCreateParams(this);
        }
    }
}
