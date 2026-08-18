package com.manycoreapis.sdk.lux3d.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Summary item returned by the Lux3D generation-record list endpoint. */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class TaskListItem {
    private final long taskId;
    private final int status;
    private final long created;
    private final long lastModified;

    @JsonCreator
    public TaskListItem(
            @JsonProperty("taskId") long taskId,
            @JsonProperty("status") int status,
            @JsonProperty("created") long created,
            @JsonProperty("lastModified") long lastModified
    ) {
        this.taskId = taskId;
        this.status = status;
        this.created = created;
        this.lastModified = lastModified;
    }

    public long taskId() { return taskId; }
    public int status() { return status; }
    public long created() { return created; }
    public long lastModified() { return lastModified; }
}
