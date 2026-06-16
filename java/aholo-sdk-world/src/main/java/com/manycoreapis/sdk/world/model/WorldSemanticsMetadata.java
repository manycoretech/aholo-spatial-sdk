package com.manycoreapis.sdk.world.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;

/** Semantic metadata for world outputs (coordinate conventions, etc.). */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class WorldSemanticsMetadata {

    private final String upAxis;

    @JsonCreator
    public WorldSemanticsMetadata(@JsonProperty("upAxis") String upAxis) {
        this.upAxis = upAxis;
    }

    /** World up axis: {@code Y} (glTF/USD) or {@code Z} (3DGS output convention). */
    public Optional<String> upAxis() { return Optional.ofNullable(upAxis); }

    @Override
    public String toString() {
        return "WorldSemanticsMetadata{upAxis='" + upAxis + "'}";
    }
}
