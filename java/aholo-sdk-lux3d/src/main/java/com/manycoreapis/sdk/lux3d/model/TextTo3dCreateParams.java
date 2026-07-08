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

    @JsonProperty("faceCount")
    private final Integer faceCount;

    @JsonProperty("needUsdz")
    private final Boolean needUsdz;

    @JsonProperty("needObj")
    private final Boolean needObj;

    @JsonProperty("needFbx")
    private final Boolean needFbx;

    private TextTo3dCreateParams(Builder b) {
        this.prompt = b.prompt;
        this.style = b.style;
        this.img = b.img;
        this.version = b.version;
        this.faceCount = b.faceCount;
        this.needUsdz = b.needUsdz;
        this.needObj = b.needObj;
        this.needFbx = b.needFbx;
    }

    public static Builder builder() { return new Builder(); }

    public String prompt()                       { return prompt; }
    public Optional<String> style()              { return Optional.ofNullable(style); }
    public Optional<String> img()                { return Optional.ofNullable(img); }
    public Optional<String> version()            { return Optional.ofNullable(version); }
    public Optional<Integer> faceCount()         { return Optional.ofNullable(faceCount); }
    public Optional<Boolean> needUsdz()          { return Optional.ofNullable(needUsdz); }
    public Optional<Boolean> needObj()           { return Optional.ofNullable(needObj); }
    public Optional<Boolean> needFbx()           { return Optional.ofNullable(needFbx); }

    public static final class Builder {
        private String prompt;
        private String style;
        private String img;
        private String version;
        private Integer faceCount;
        private Boolean needUsdz;
        private Boolean needObj;
        private Boolean needFbx;

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
         * Model version: {@code "v3.0-standard"} (default), {@code "v2.0-preview"}, or {@code "v1.0-pro"}.
         */
        public Builder version(String version)     { this.version = version; return this; }
        /** Target face count for v3.0-standard only (10_000–500_000). */
        public Builder faceCount(Integer faceCount){ this.faceCount = faceCount; return this; }
        public Builder needUsdz(Boolean needUsdz)  { this.needUsdz = needUsdz; return this; }
        public Builder needObj(Boolean needObj)    { this.needObj = needObj; return this; }
        public Builder needFbx(Boolean needFbx)  { this.needFbx = needFbx; return this; }

        public TextTo3dCreateParams build() {
            Objects.requireNonNull(prompt, "prompt is required");
            return new TextTo3dCreateParams(this);
        }
    }
}
