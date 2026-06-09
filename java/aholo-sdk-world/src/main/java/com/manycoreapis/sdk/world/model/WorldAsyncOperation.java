package com.manycoreapis.sdk.world.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Response returned when a world task is created asynchronously. */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class WorldAsyncOperation {

    private final String worldId;

    @JsonCreator
    public WorldAsyncOperation(@JsonProperty("worldId") String worldId) {
        this.worldId = worldId;
    }

    public String worldId() { return worldId; }

    @Override
    public String toString() {
        return "WorldAsyncOperation{worldId='" + worldId + "'}";
    }
}
