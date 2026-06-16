package com.manycoreapis.sdk.world.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class WorldAssetBundle {

    private final WorldSplatBundle splats;
    private final WorldImagery imagery;
    private final WorldSemanticsMetadata semanticsMetadata;

    @JsonCreator
    public WorldAssetBundle(
            @JsonProperty("splats")             WorldSplatBundle splats,
            @JsonProperty("imagery")            WorldImagery imagery,
            @JsonProperty("semanticsMetadata")  WorldSemanticsMetadata semanticsMetadata
    ) {
        this.splats = splats;
        this.imagery = imagery;
        this.semanticsMetadata = semanticsMetadata;
    }

    public Optional<WorldSplatBundle> splats()              { return Optional.ofNullable(splats); }
    public Optional<WorldImagery> imagery()                 { return Optional.ofNullable(imagery); }
    public Optional<WorldSemanticsMetadata> semanticsMetadata() { return Optional.ofNullable(semanticsMetadata); }

    @Override
    public String toString() {
        return "WorldAssetBundle{splats=" + splats + ", imagery=" + imagery
                + ", semanticsMetadata=" + semanticsMetadata + "}";
    }
}
