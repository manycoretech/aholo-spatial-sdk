package com.manycoreapis.sdk.lux3d.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.Optional;

/** Parameters for creating a material-transfer task. */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class MaterialTransferCreateParams {

    @JsonProperty("img")
    private final String img;

    @JsonProperty("meshUrl")
    private final String meshUrl;

    @JsonProperty("version")
    private final String version;

    @JsonProperty("needUsdz")
    private final Boolean needUsdz;

    @JsonProperty("needObj")
    private final Boolean needObj;

    @JsonProperty("needFbx")
    private final Boolean needFbx;

    private MaterialTransferCreateParams(Builder b) {
        this.img = b.img;
        this.meshUrl = b.meshUrl;
        this.version = b.version;
        this.needUsdz = b.needUsdz;
        this.needObj = b.needObj;
        this.needFbx = b.needFbx;
    }

    public static Builder builder() { return new Builder(); }

    public String img()                          { return img; }
    public String meshUrl()                      { return meshUrl; }
    public Optional<String> version()            { return Optional.ofNullable(version); }
    public Optional<Boolean> needUsdz()          { return Optional.ofNullable(needUsdz); }
    public Optional<Boolean> needObj()           { return Optional.ofNullable(needObj); }
    public Optional<Boolean> needFbx()           { return Optional.ofNullable(needFbx); }

    public static final class Builder {
        private String img;
        private String meshUrl;
        private String version;
        private Boolean needUsdz;
        private Boolean needObj;
        private Boolean needFbx;

        private Builder() {}

        /** Reference material image (base64 data-URL or remote URL). */
        public Builder img(String img)             { this.img = img; return this; }
        /** URL of the target mesh to apply the material to. */
        public Builder meshUrl(String meshUrl)   { this.meshUrl = meshUrl; return this; }
        /**
         * Model version: {@code "v3.0-standard"} (default), {@code "v2.0-preview"}, or {@code "v1.0-pro"}.
         */
        public Builder version(String version)     { this.version = version; return this; }
        public Builder needUsdz(Boolean needUsdz)  { this.needUsdz = needUsdz; return this; }
        public Builder needObj(Boolean needObj)    { this.needObj = needObj; return this; }
        public Builder needFbx(Boolean needFbx)  { this.needFbx = needFbx; return this; }

        public MaterialTransferCreateParams build() {
            Objects.requireNonNull(img,     "img is required");
            Objects.requireNonNull(meshUrl, "meshUrl is required");
            return new MaterialTransferCreateParams(this);
        }
    }
}
