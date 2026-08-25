package com.manycoreapis.sdk.lux3d.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/** Parameters for creating a single-image-to-four-view task. */
public final class ImageToFourViewCreateParams {
    @JsonProperty("img") private final String img;

    private ImageToFourViewCreateParams(Builder builder) { this.img = builder.img; }
    public static Builder builder() { return new Builder(); }
    public String img() { return img; }

    public static final class Builder {
        private String img;
        private Builder() {}
        public Builder img(String img) { this.img = img; return this; }
        public ImageToFourViewCreateParams build() {
            Objects.requireNonNull(img, "img is required");
            if (img.trim().isEmpty()) throw new IllegalArgumentException("img must not be empty");
            return new ImageToFourViewCreateParams(this);
        }
    }
}
