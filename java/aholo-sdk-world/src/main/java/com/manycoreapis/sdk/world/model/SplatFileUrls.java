package com.manycoreapis.sdk.world.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;

/** URLs for the 3DGS splat files produced by a reconstruction task. */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class SplatFileUrls {

    private final String plyPath;
    private final String spzPath;
    private final String lodMetaPath;

    @JsonCreator
    public SplatFileUrls(
            @JsonProperty("plyPath")     String plyPath,
            @JsonProperty("spzPath")     String spzPath,
            @JsonProperty("lodMetaPath") String lodMetaPath
    ) {
        this.plyPath = plyPath;
        this.spzPath = spzPath;
        this.lodMetaPath = lodMetaPath;
    }

    public Optional<String> plyPath()     { return Optional.ofNullable(plyPath); }
    public Optional<String> spzPath()     { return Optional.ofNullable(spzPath); }
    public Optional<String> lodMetaPath() { return Optional.ofNullable(lodMetaPath); }

    @Override
    public String toString() {
        return "SplatFileUrls{plyPath='" + plyPath + "', spzPath='" + spzPath + "', lodMetaPath='" + lodMetaPath + "'}";
    }
}
