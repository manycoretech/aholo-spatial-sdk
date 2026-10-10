package com.manycoreapis.sdk.lux3d.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Parameters for creating a humanoid auto-rig task. */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class HumanoidAutoRigCreateParams {
    @JsonProperty("modelUrl") private final String modelUrl;

    private HumanoidAutoRigCreateParams(Builder builder) {
        this.modelUrl = builder.modelUrl;
    }
    public static Builder builder() { return new Builder(); }
    public String modelUrl() { return modelUrl; }

    public static final class Builder {
        private String modelUrl;
        private Builder() {}
        public Builder modelUrl(String modelUrl) { this.modelUrl = modelUrl; return this; }
        public HumanoidAutoRigCreateParams build() {
            if (modelUrl == null || modelUrl.trim().isEmpty()) {
                throw new IllegalArgumentException("modelUrl is required");
            }
            return new HumanoidAutoRigCreateParams(this);
        }
    }
}
