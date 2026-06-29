package com.manycoreapis.sdk.world.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/** An image resource for world generation tasks (Spatial Gen). */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class GenerateWorldResource {

    @JsonProperty("url")
    private final String url;

    @JsonProperty("type")
    private final String type;

    private GenerateWorldResource(String url, String type) {
        this.url = Objects.requireNonNull(url, "url is required");
        this.type = type;
    }

    public static GenerateWorldResource of(String url) {
        return new GenerateWorldResource(url, null);
    }

    public static GenerateWorldResource of(String url, String type) {
        return new GenerateWorldResource(url, type);
    }

    /** Image resource URL (e.g. returned by AssetClient.uploadFile). */
    public String url() { return url; }

    /** Resource type: {@code "image"} only; omitted defaults to image on the server. */
    public String type() { return type; }

    @Override
    public String toString() {
        return "GenerateWorldResource{url='" + url + "', type='" + type + "'}";
    }
}
