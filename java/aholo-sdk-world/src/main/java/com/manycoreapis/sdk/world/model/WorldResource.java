package com.manycoreapis.sdk.world.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/** A media resource (image or video) associated with a world task. */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class WorldResource {

    @JsonProperty("url")
    private final String url;

    @JsonProperty("type")
    private final String type;

    private WorldResource(String url, String type) {
        this.url = Objects.requireNonNull(url, "url is required");
        this.type = type;
    }

    public static WorldResource of(String url) {
        return new WorldResource(url, null);
    }

    public static WorldResource of(String url, String type) {
        return new WorldResource(url, type);
    }

    /** Resource URL (e.g. returned by AssetClient.uploadFile). */
    public String url() { return url; }

    /** Resource type: {@code "image"} or {@code "video"}. */
    public String type() { return type; }

    @Override
    public String toString() {
        return "WorldResource{url='" + url + "', type='" + type + "'}";
    }
}
