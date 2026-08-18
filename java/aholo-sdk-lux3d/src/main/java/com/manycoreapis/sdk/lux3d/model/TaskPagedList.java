package com.manycoreapis.sdk.lux3d.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

/** Paginated Lux3D generation records. */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class TaskPagedList {
    private final List<TaskListItem> items;
    private final int total;
    private final int page;
    private final int pageSize;

    @JsonCreator
    public TaskPagedList(
            @JsonProperty("items") List<TaskListItem> items,
            @JsonProperty("total") int total,
            @JsonProperty("page") int page,
            @JsonProperty("pageSize") int pageSize
    ) {
        this.items = items == null ? Collections.<TaskListItem>emptyList() : Collections.unmodifiableList(items);
        this.total = total;
        this.page = page;
        this.pageSize = pageSize;
    }

    public List<TaskListItem> items() { return items; }
    public int total() { return total; }
    public int page() { return page; }
    public int pageSize() { return pageSize; }
}
