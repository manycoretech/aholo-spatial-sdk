package com.manycoreapis.sdk.world.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/** Parameters for creating a world generation task. */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class GenerationCreateParams {

    @JsonProperty("name")
    private final String name;

    @JsonProperty("cover")
    private final String cover;

    @JsonProperty("resources")
    private final List<WorldResource> resources;

    @JsonProperty("prompt")
    private final String prompt;

    private GenerationCreateParams(Builder b) {
        this.name = b.name;
        this.cover = b.cover;
        this.resources = b.resources.isEmpty()
                ? null
                : Collections.unmodifiableList(new ArrayList<>(b.resources));
        this.prompt = b.prompt;
    }

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private String name;
        private String cover;
        private final List<WorldResource> resources = new ArrayList<>();
        private String prompt;

        private Builder() {}

        public Builder name(String name)     { this.name = name; return this; }
        public Builder cover(String cover)   { this.cover = cover; return this; }
        public Builder prompt(String prompt) { this.prompt = prompt; return this; }

        public Builder resources(List<WorldResource> resources) {
            Objects.requireNonNull(resources, "resources");
            this.resources.clear();
            this.resources.addAll(resources);
            return this;
        }

        public Builder addResource(WorldResource resource) {
            this.resources.add(Objects.requireNonNull(resource, "resource"));
            return this;
        }

        public Builder addResource(String url, String type) {
            return addResource(WorldResource.of(url, type));
        }

        public GenerationCreateParams build() {
            return new GenerationCreateParams(this);
        }
    }
}
