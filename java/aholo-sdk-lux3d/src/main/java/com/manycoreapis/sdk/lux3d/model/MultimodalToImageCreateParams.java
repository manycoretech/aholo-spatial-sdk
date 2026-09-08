package com.manycoreapis.sdk.lux3d.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;

/** Parameters for creating a multimodal single-image generation task. */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class MultimodalToImageCreateParams {
    @JsonProperty("img") private final String img;
    @JsonProperty("prompt") private final String prompt;

    private MultimodalToImageCreateParams(Builder builder) {
        this.img = builder.img;
        this.prompt = builder.prompt;
    }
    public static Builder builder() { return new Builder(); }
    public Optional<String> img() { return Optional.ofNullable(img); }
    public Optional<String> prompt() { return Optional.ofNullable(prompt); }

    public static final class Builder {
        private String img;
        private String prompt;
        private Builder() {}
        public Builder img(String img) { this.img = img; return this; }
        public Builder prompt(String prompt) { this.prompt = prompt; return this; }
        public MultimodalToImageCreateParams build() {
            if (img != null && img.trim().isEmpty()) {
                throw new IllegalArgumentException("img must not be empty");
            }
            if (prompt != null && prompt.trim().isEmpty()) {
                throw new IllegalArgumentException("prompt must not be empty");
            }
            if (img == null && prompt == null) {
                throw new IllegalArgumentException("img or prompt is required");
            }
            return new MultimodalToImageCreateParams(this);
        }
    }
}
