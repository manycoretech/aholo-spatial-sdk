package com.manycoreapis.sdk.world.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/** Paginated list of worlds returned by {@link com.manycoreapis.sdk.world.WorldClient#list}. */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class WorldPagedList {

    private final Integer pageNum;
    private final Integer pageSize;
    private final Integer count;
    private final Integer totalCount;
    private final Boolean hasMore;
    private final List<WorldDetail> result;

    @JsonCreator
    public WorldPagedList(
            @JsonProperty("pageNum")    Integer pageNum,
            @JsonProperty("pageSize")   Integer pageSize,
            @JsonProperty("count")      Integer count,
            @JsonProperty("totalCount") Integer totalCount,
            @JsonProperty("hasMore")    Boolean hasMore,
            @JsonProperty("result")     List<WorldDetail> result
    ) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.count = count;
        this.totalCount = totalCount;
        this.hasMore = hasMore;
        this.result = result == null ? Collections.emptyList() : Collections.unmodifiableList(result);
    }

    public Optional<Integer> pageNum()    { return Optional.ofNullable(pageNum); }
    public Optional<Integer> pageSize()   { return Optional.ofNullable(pageSize); }
    public Optional<Integer> count()      { return Optional.ofNullable(count); }
    public Optional<Integer> totalCount() { return Optional.ofNullable(totalCount); }
    public Optional<Boolean> hasMore()    { return Optional.ofNullable(hasMore); }
    public List<WorldDetail> result()     { return result; }

    @Override
    public String toString() {
        return "WorldPagedList{totalCount=" + totalCount + ", hasMore=" + hasMore + ", result.size=" + result.size() + "}";
    }
}
