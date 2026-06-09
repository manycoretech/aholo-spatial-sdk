package com.manycoreapis.sdk.world.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class WorldAssetBundle {

    private final WorldSplatBundle splats;
    private final String lodMetaPath;

    @JsonCreator
    public WorldAssetBundle(
            @JsonProperty("splats")      WorldSplatBundle splats,
            @JsonProperty("lodMetaPath") String lodMetaPath
    ) {
        this.splats = splats;
        this.lodMetaPath = lodMetaPath;
    }

    public Optional<WorldSplatBundle> splats()     { return Optional.ofNullable(splats); }
    public Optional<String>           lodMetaPath() { return Optional.ofNullable(lodMetaPath); }

    @Override
    public String toString() {
        return "WorldAssetBundle{splats=" + splats + ", lodMetaPath='" + lodMetaPath + "'}";
    }
}
