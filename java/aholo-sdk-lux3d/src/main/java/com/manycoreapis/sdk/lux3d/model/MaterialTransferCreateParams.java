package com.manycoreapis.sdk.lux3d.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/** Parameters for creating a material-transfer task. */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class MaterialTransferCreateParams {

    @JsonProperty("img")
    private final String img;

    @JsonProperty("meshUrl")
    private final String meshUrl;

    @JsonProperty("version")
    private final String version;

    private MaterialTransferCreateParams(Builder b) {
        this.img = b.img;
        this.meshUrl = b.meshUrl;
        this.version = b.version;
    }

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private String img;
        private String meshUrl;
        private String version;

        private Builder() {}

        /** Reference material image (base64 data-URL or remote URL). */
        public Builder img(String img)         { this.img = img; return this; }
        /** URL of the target mesh to apply the material to. */
        public Builder meshUrl(String meshUrl) { this.meshUrl = meshUrl; return this; }
        public Builder version(String version) { this.version = version; return this; }

        public MaterialTransferCreateParams build() {
            Objects.requireNonNull(img,     "img is required");
            Objects.requireNonNull(meshUrl, "meshUrl is required");
            return new MaterialTransferCreateParams(this);
        }
    }
}
