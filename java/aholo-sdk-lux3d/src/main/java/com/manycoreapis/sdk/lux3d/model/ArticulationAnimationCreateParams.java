package com.manycoreapis.sdk.lux3d.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Parameters for creating an articulation and animation task. */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ArticulationAnimationCreateParams {
    @JsonProperty("glbUrl") private final String glbUrl;
    @JsonProperty("prompt") private final String prompt;

    private ArticulationAnimationCreateParams(Builder builder) {
        this.glbUrl = builder.glbUrl;
        this.prompt = builder.prompt;
    }
    public static Builder builder() { return new Builder(); }
    public String glbUrl() { return glbUrl; }
    public String prompt() { return prompt; }

    public static final class Builder {
        private String glbUrl;
        private String prompt;
        private Builder() {}
        public Builder glbUrl(String glbUrl) { this.glbUrl = glbUrl; return this; }
        public Builder prompt(String prompt) { this.prompt = prompt; return this; }
        public ArticulationAnimationCreateParams build() {
            if (glbUrl == null || glbUrl.trim().isEmpty() || prompt == null || prompt.trim().isEmpty()) {
                throw new IllegalArgumentException("glbUrl and prompt are required");
            }
            return new ArticulationAnimationCreateParams(this);
        }
    }
}
