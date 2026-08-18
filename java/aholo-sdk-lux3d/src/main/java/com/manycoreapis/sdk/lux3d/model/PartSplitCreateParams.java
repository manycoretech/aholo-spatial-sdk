package com.manycoreapis.sdk.lux3d.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/** Parameters for creating a GLB part-split task. */
public final class PartSplitCreateParams {

    @JsonProperty("glbUrl")
    private final String glbUrl;

    private PartSplitCreateParams(Builder builder) {
        this.glbUrl = builder.glbUrl;
    }

    public static Builder builder() { return new Builder(); }

    public String glbUrl() { return glbUrl; }

    public static final class Builder {
        private String glbUrl;

        private Builder() {}

        /** Public URL of the GLB model to split. */
        public Builder glbUrl(String glbUrl) { this.glbUrl = glbUrl; return this; }

        public PartSplitCreateParams build() {
            Objects.requireNonNull(glbUrl, "glbUrl is required");
            if (glbUrl.trim().isEmpty()) throw new IllegalArgumentException("glbUrl must not be empty");
            return new PartSplitCreateParams(this);
        }
    }
}
