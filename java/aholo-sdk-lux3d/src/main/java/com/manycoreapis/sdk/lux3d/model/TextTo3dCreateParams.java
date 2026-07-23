package com.manycoreapis.sdk.lux3d.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
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

    @JsonProperty("faceCount")
    private final Integer faceCount;

    @JsonProperty("outputFormat")
    private final List<String> outputFormat;

    @JsonProperty("enablePbr")
    private final Boolean enablePbr;

    @JsonProperty("textureSize")
    private final Integer textureSize;

    private TextTo3dCreateParams(Builder b) {
        this.prompt = b.prompt;
        this.style = b.style;
        this.img = b.img;
        this.version = b.version;
        this.faceCount = b.faceCount;
        this.outputFormat = b.outputFormat;
        this.enablePbr = b.enablePbr;
        this.textureSize = b.textureSize;
    }

    public static Builder builder() { return new Builder(); }

    public String prompt()                       { return prompt; }
    public Optional<String> style()              { return Optional.ofNullable(style); }
    public Optional<String> img()                { return Optional.ofNullable(img); }
    public Optional<String> version()            { return Optional.ofNullable(version); }
    public Optional<Integer> faceCount()         { return Optional.ofNullable(faceCount); }
    public Optional<List<String>> outputFormat() { return Optional.ofNullable(outputFormat); }
    public Optional<Boolean> enablePbr()         { return Optional.ofNullable(enablePbr); }
    public Optional<Integer> textureSize()       { return Optional.ofNullable(textureSize); }

    public static final class Builder {
        private String prompt;
        private String style;
        private String img;
        private String version;
        private Integer faceCount;
        private List<String> outputFormat;
        private Boolean enablePbr;
        private Integer textureSize;

        private Builder() {}

        public Builder prompt(String prompt)       { this.prompt = prompt; return this; }
        /**
         * Generation style. One of: {@code photorealistic}, {@code cartoon}, {@code anime},
         * {@code hand_painted}, {@code cyberpunk}, {@code fantasy}, {@code glass}.
         * Defaults to {@code photorealistic}.
         */
        public Builder style(String style)         { this.style = style; return this; }
        /** Optional reference image (base64 data-URL or remote URL). */
        public Builder img(String img)             { this.img = img; return this; }
        /**
         * Model version: {@code "v3.0-standard"} (default), {@code "v2.0-preview"},
         * {@code "v1.0-pro"}, or {@code "G1"}.
         */
        public Builder version(String version)     { this.version = version; return this; }
        /** Target face count for v2 / v3 / G1 (10_000–500_000). */
        public Builder faceCount(Integer faceCount){ this.faceCount = faceCount; return this; }
        public Builder outputFormat(List<String> outputFormat){ this.outputFormat = outputFormat; return this; }
        public Builder enablePbr(Boolean enablePbr){ this.enablePbr = enablePbr; return this; }
        public Builder textureSize(Integer textureSize){ this.textureSize = textureSize; return this; }

        public TextTo3dCreateParams build() {
            Objects.requireNonNull(prompt, "prompt is required");
            return new TextTo3dCreateParams(this);
        }
    }
}
