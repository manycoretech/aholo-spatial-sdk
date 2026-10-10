package com.manycoreapis.sdk.lux3d.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Parameters for creating a part-split task. */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class PartSplitCreateParams {
    @JsonProperty("glbUrl") private final String glbUrl;

    private PartSplitCreateParams(Builder builder) {
        this.glbUrl = builder.glbUrl;
    }
    public static Builder builder() { return new Builder(); }
    public String glbUrl() { return glbUrl; }

    public static final class Builder {
        private String glbUrl;
        private Builder() {}
        public Builder glbUrl(String glbUrl) { this.glbUrl = glbUrl; return this; }
        public PartSplitCreateParams build() {
            if (glbUrl == null || glbUrl.trim().isEmpty()) {
                throw new IllegalArgumentException("glbUrl is required");
            }
            return new PartSplitCreateParams(this);
        }
    }
}
