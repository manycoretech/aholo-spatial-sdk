package com.manycoreapis.sdk.lux3d.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.Optional;

/** Parameters for creating a text-to-3D task. */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class TextTo3dCreateParams {

    @JsonProperty("prompt")
    private final String prompt;

    @JsonProperty("style")
    private final String style;

    @JsonProperty("img")
    private final String img;

    @JsonProperty("version")
    private final String version;

    private TextTo3dCreateParams(Builder b) {
        this.prompt = b.prompt;
        this.style = b.style;
        this.img = b.img;
        this.version = b.version;
    }

    public static Builder builder() { return new Builder(); }

    public String prompt()                       { return prompt; }
    public Optional<String> style()              { return Optional.ofNullable(style); }
    public Optional<String> img()                { return Optional.ofNullable(img); }
    public Optional<String> version()            { return Optional.ofNullable(version); }

    public static final class Builder {
        private String prompt;
        private String style;
        private String img;
        private String version;

        private Builder() {}

        public Builder prompt(String prompt)   { this.prompt = prompt; return this; }
        /**
         * Generation style. One of: {@code photorealistic}, {@code cartoon}, {@code anime},
         * {@code hand_painted}, {@code cyberpunk}, {@code fantasy}, {@code glass}.
         * Defaults to {@code photorealistic}.
         */
        public Builder style(String style)     { this.style = style; return this; }
        /** Optional reference image (base64 data-URL or remote URL). */
        public Builder img(String img)         { this.img = img; return this; }
        public Builder version(String version) { this.version = version; return this; }

        public TextTo3dCreateParams build() {
            Objects.requireNonNull(prompt, "prompt is required");
            return new TextTo3dCreateParams(this);
        }
    }
}
