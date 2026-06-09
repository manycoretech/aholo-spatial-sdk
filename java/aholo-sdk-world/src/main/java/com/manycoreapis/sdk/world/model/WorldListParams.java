package com.manycoreapis.sdk.world.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/** Parameters for listing worlds with optional pagination and status filter. */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class WorldListParams {

    @JsonProperty("pageNum")
    private final Integer pageNum;

    @JsonProperty("pageSize")
    private final Integer pageSize;

    @JsonProperty("statusList")
    private final List<String> statusList;

    private WorldListParams(Builder b) {
        this.pageNum = b.pageNum;
        this.pageSize = b.pageSize;
        this.statusList = b.statusList.isEmpty()
                ? null
                : Collections.unmodifiableList(new ArrayList<>(b.statusList));
    }

    /** Returns an empty params object (no filters, default pagination). */
    public static WorldListParams empty() { return builder().build(); }

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private Integer pageNum;
        private Integer pageSize;
        private final List<String> statusList = new ArrayList<>();

        private Builder() {}

        public Builder pageNum(int pageNum)   { this.pageNum = pageNum; return this; }
        public Builder pageSize(int pageSize) { this.pageSize = pageSize; return this; }

        public Builder statusList(List<String> statuses) {
            Objects.requireNonNull(statuses, "statuses");
            this.statusList.clear();
            this.statusList.addAll(statuses);
            return this;
        }

        public Builder addStatus(String status) {
            this.statusList.add(Objects.requireNonNull(status, "status"));
            return this;
        }

        public WorldListParams build() { return new WorldListParams(this); }
    }
}
