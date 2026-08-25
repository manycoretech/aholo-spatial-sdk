package com.manycoreapis.sdk.lux3d.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

/**
 * Result of a Lux3D task query.
 *
 * <p>Status codes: 0=init, 1=running, 3=success, 4=failed, 6=canceled.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class TaskResult {

    private final long taskId;
    private final String bizId;
    private final int  status;
    private final List<TaskOutput> outputs;

    @JsonCreator
    public TaskResult(
            @JsonProperty("taskId")  long taskId,
            @JsonProperty("bizId")   String bizId,
            @JsonProperty("status")  int status,
            @JsonProperty("outputs") List<TaskOutput> outputs
    ) {
        this.taskId = taskId;
        this.bizId = bizId;
        this.status = status;
        this.outputs = outputs == null ? Collections.emptyList() : Collections.unmodifiableList(outputs);
    }

    public long taskId()              { return taskId; }
    public String bizId()             { return bizId; }
    public int  status()              { return status; }
    public List<TaskOutput> outputs() { return outputs; }

    @Override
    public String toString() {
        return "TaskResult{taskId=" + taskId + ", bizId='" + bizId + "', status=" + status + ", outputs=" + outputs + "}";
    }
}
