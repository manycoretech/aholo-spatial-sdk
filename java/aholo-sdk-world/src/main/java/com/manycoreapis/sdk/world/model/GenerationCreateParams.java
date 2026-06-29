package com.manycoreapis.sdk.world.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/** Parameters for creating a world generation task. */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class GenerationCreateParams {

    @JsonProperty("name")
    private final String name;

    @JsonProperty("cover")
    private final String cover;

    @JsonProperty("resources")
    private final List<GenerateWorldResource> resources;

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

    public Optional<String> name()                      { return Optional.ofNullable(name); }
    public Optional<String> cover()                     { return Optional.ofNullable(cover); }
    public Optional<String> prompt()                    { return Optional.ofNullable(prompt); }
    public Optional<List<GenerateWorldResource>> resources()    { return Optional.ofNullable(resources); }

    public static final class Builder {
        private String name;
        private String cover;
        private final List<GenerateWorldResource> resources = new ArrayList<>();
        private String prompt;

        private Builder() {}

        public Builder name(String name)     { this.name = name; return this; }
        public Builder cover(String cover)   { this.cover = cover; return this; }
        public Builder prompt(String prompt) { this.prompt = prompt; return this; }

        public Builder resources(List<GenerateWorldResource> resources) {
            Objects.requireNonNull(resources, "resources");
            this.resources.clear();
            this.resources.addAll(resources);
            return this;
        }

        public Builder addResource(GenerateWorldResource resource) {
            this.resources.add(Objects.requireNonNull(resource, "resource"));
            return this;
        }

        public Builder addResource(String url, String type) {
            return addResource(GenerateWorldResource.of(url, type));
        }

        public GenerationCreateParams build() {
            return new GenerationCreateParams(this);
        }
    }
}
