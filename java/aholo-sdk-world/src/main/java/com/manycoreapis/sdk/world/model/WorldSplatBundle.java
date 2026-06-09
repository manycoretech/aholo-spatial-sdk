package com.manycoreapis.sdk.world.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class WorldSplatBundle {

    private final SplatFileUrls urls;

    @JsonCreator
    public WorldSplatBundle(@JsonProperty("urls") SplatFileUrls urls) {
        this.urls = urls;
    }

    public Optional<SplatFileUrls> urls() { return Optional.ofNullable(urls); }

    @Override
    public String toString() {
        return "WorldSplatBundle{urls=" + urls + "}";
    }
}
