package com.manycoreapis.sdk.world.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/** Parameters for creating a 3DGS reconstruction world task. */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ReconstructionCreateParams {

    @JsonProperty("name")
    private final String name;

    @JsonProperty("cover")
    private final String cover;

    @JsonProperty("resources")
    private final List<WorldResource> resources;

    @JsonProperty("taskQuality")
    private final String taskQuality;

    @JsonProperty("scene")
    private final String scene;

    private ReconstructionCreateParams(Builder b) {
        this.name = b.name;
        this.cover = b.cover;
        this.resources = Collections.unmodifiableList(new ArrayList<>(b.resources));
        this.taskQuality = b.taskQuality;
        this.scene = b.scene;
    }

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private String name;
        private String cover;
        private final List<WorldResource> resources = new ArrayList<>();
        private String taskQuality;
        private String scene;

        private Builder() {}

        public Builder name(String name)               { this.name = name; return this; }
        public Builder cover(String cover)             { this.cover = cover; return this; }
        public Builder taskQuality(String taskQuality) { this.taskQuality = taskQuality; return this; }
        public Builder scene(String scene)             { this.scene = scene; return this; }

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

        public ReconstructionCreateParams build() {
            if (resources.isEmpty()) throw new IllegalStateException("resources must not be empty");
            Objects.requireNonNull(taskQuality, "taskQuality is required");
            Objects.requireNonNull(scene, "scene is required");
            return new ReconstructionCreateParams(this);
        }
    }
}
