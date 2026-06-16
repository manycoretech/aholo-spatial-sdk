package com.manycoreapis.sdk.world.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;

/** AI-generated world imagery outputs (e.g. panorama URL). */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class WorldImagery {

    private final String panoUrl;

    @JsonCreator
    public WorldImagery(@JsonProperty("panoUrl") String panoUrl) {
        this.panoUrl = panoUrl;
    }

    public Optional<String> panoUrl() { return Optional.ofNullable(panoUrl); }

    @Override
    public String toString() {
        return "WorldImagery{panoUrl='" + panoUrl + "'}";
    }
}
