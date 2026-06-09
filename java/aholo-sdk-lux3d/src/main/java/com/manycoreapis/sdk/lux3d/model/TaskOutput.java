package com.manycoreapis.sdk.lux3d.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;

/** A single output artifact from a completed Lux3D task. */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class TaskOutput {

    private final String content;

    @JsonCreator
    public TaskOutput(@JsonProperty("content") String content) {
        this.content = content;
    }

    /** URL of the output file (e.g. .glb, .zip, .usdz, .lux3d). */
    public Optional<String> content() { return Optional.ofNullable(content); }

    @Override
    public String toString() {
        return "TaskOutput{content='" + content + "'}";
    }
}
