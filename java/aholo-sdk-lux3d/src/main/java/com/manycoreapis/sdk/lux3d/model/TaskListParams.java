package com.manycoreapis.sdk.lux3d.model;

import java.util.Optional;

/** Optional filters for listing Lux3D generation records. */
public final class TaskListParams {
    private final Integer page;
    private final Integer pageSize;
    private final Integer status;
    private final Long startTime;
    private final Long endTime;

    private TaskListParams(Builder builder) {
        this.page = builder.page;
        this.pageSize = builder.pageSize;
        this.status = builder.status;
        this.startTime = builder.startTime;
        this.endTime = builder.endTime;
    }

    public static TaskListParams empty() { return builder().build(); }
    public static Builder builder() { return new Builder(); }

    public Optional<Integer> page() { return Optional.ofNullable(page); }
    public Optional<Integer> pageSize() { return Optional.ofNullable(pageSize); }
    public Optional<Integer> status() { return Optional.ofNullable(status); }
    public Optional<Long> startTime() { return Optional.ofNullable(startTime); }
    public Optional<Long> endTime() { return Optional.ofNullable(endTime); }

    public static final class Builder {
        private Integer page;
        private Integer pageSize;
        private Integer status;
        private Long startTime;
        private Long endTime;

        private Builder() {}

        public Builder page(int page) { this.page = page; return this; }
        public Builder pageSize(int pageSize) { this.pageSize = pageSize; return this; }
        public Builder status(int status) { this.status = status; return this; }
        public Builder startTime(long startTime) { this.startTime = startTime; return this; }
        public Builder endTime(long endTime) { this.endTime = endTime; return this; }

        public TaskListParams build() {
            if (page != null && page < 1) throw new IllegalArgumentException("page must be >= 1");
            if (pageSize != null && (pageSize < 1 || pageSize > 100)) {
                throw new IllegalArgumentException("pageSize must be between 1 and 100");
            }
            if (status != null && status != 0 && status != 1 && status != 3 && status != 4) {
                throw new IllegalArgumentException("status must be one of 0, 1, 3, 4");
            }
            if (startTime != null && startTime < 0) throw new IllegalArgumentException("startTime must be >= 0");
            if (endTime != null && endTime < 0) throw new IllegalArgumentException("endTime must be >= 0");
            return new TaskListParams(this);
        }
    }
}
