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

    @JsonProperty("faceCount")
    private final Integer faceCount;

    @JsonProperty("needUsdz")
    private final Boolean needUsdz;

    @JsonProperty("needObj")
    private final Boolean needObj;

    @JsonProperty("needFbx")
    private final Boolean needFbx;

    private ImgTo3dCreateParams(Builder b) {
        this.img = b.img;
        this.version = b.version;
        this.faceCount = b.faceCount;
        this.needUsdz = b.needUsdz;
        this.needObj = b.needObj;
        this.needFbx = b.needFbx;
    }

    public static Builder builder() { return new Builder(); }

    public String img()                          { return img; }
    public Optional<String> version()            { return Optional.ofNullable(version); }
    public Optional<Integer> faceCount()         { return Optional.ofNullable(faceCount); }
    public Optional<Boolean> needUsdz()          { return Optional.ofNullable(needUsdz); }
    public Optional<Boolean> needObj()           { return Optional.ofNullable(needObj); }
    public Optional<Boolean> needFbx()           { return Optional.ofNullable(needFbx); }

    public static final class Builder {
        private String img;
        private String version;
        private Integer faceCount;
        private Boolean needUsdz;
        private Boolean needObj;
        private Boolean needFbx;

        private Builder() {}

        /** Base64 data-URL or remote URL of the input image. */
        public Builder img(String img)             { this.img = img; return this; }
        /**
         * Model version: {@code "v3.0-standard"} (default), {@code "v2.0-preview"}, or {@code "v1.0-pro"}.
         */
        public Builder version(String version)     { this.version = version; return this; }
        /** Target face count for v3.0-standard only (10_000–500_000). */
        public Builder faceCount(Integer faceCount){ this.faceCount = faceCount; return this; }
        /** Request USDZ export (v3.0-standard optional slot outputs[2]). */
        public Builder needUsdz(Boolean needUsdz)  { this.needUsdz = needUsdz; return this; }
        /** Request OBJ zip export (v3.0-standard optional slot outputs[3]). */
        public Builder needObj(Boolean needObj)    { this.needObj = needObj; return this; }
        /** Request FBX zip export (v3.0-standard optional slot outputs[4]). */
        public Builder needFbx(Boolean needFbx)  { this.needFbx = needFbx; return this; }

        public ImgTo3dCreateParams build() {
            Objects.requireNonNull(img, "img is required");
            return new ImgTo3dCreateParams(this);
        }
    }
}
