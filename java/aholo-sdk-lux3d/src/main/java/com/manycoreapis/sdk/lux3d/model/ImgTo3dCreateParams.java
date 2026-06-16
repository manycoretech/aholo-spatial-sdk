package com.manycoreapis.sdk.lux3d.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.Optional;

/** Parameters for creating an image-to-3D task. */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ImgTo3dCreateParams {

    @JsonProperty("img")
    private final String img;

    @JsonProperty("version")
    private final String version;

    private ImgTo3dCreateParams(Builder b) {
        this.img = b.img;
        this.version = b.version;
    }

    public static Builder builder() { return new Builder(); }

    public String img()                          { return img; }
    public Optional<String> version()            { return Optional.ofNullable(version); }

    public static final class Builder {
        private String img;
        private String version;

        private Builder() {}

        /** Base64 data-URL or remote URL of the input image. */
        public Builder img(String img)         { this.img = img; return this; }
        /** Model version, e.g. {@code "v2.0-preview"} (default) or {@code "v1.0-pro"}. */
        public Builder version(String version) { this.version = version; return this; }

        public ImgTo3dCreateParams build() {
            Objects.requireNonNull(img, "img is required");
            return new ImgTo3dCreateParams(this);
        }
    }
}
