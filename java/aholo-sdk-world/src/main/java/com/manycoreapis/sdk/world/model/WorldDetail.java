package com.manycoreapis.sdk.world.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;

/** Full detail of a world, returned by {@link com.manycoreapis.sdk.world.WorldClient#retrieve} and {@link com.manycoreapis.sdk.world.WorldClient#waitFor}. */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class WorldDetail {

    private final String           worldId;
    private final String           name;
    private final String           cover;
    private final String           scene;
    private final Long             createTime;
    private final Long             updateTime;
    private final String           status;
    private final Double           progress;
    private final WorldAssetBundle assets;

    @JsonCreator
    public WorldDetail(
            @JsonProperty("worldId")    String worldId,
            @JsonProperty("name")       String name,
            @JsonProperty("cover")      String cover,
            @JsonProperty("scene")      String scene,
            @JsonProperty("createTime") Long createTime,
            @JsonProperty("updateTime") Long updateTime,
            @JsonProperty("status")     String status,
            @JsonProperty("progress")   Double progress,
            @JsonProperty("assets")     WorldAssetBundle assets
    ) {
        this.worldId = worldId;
        this.name = name;
        this.cover = cover;
        this.scene = scene;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.status = status;
        this.progress = progress;
        this.assets = assets;
    }

    public String worldId()                       { return worldId; }
    public Optional<String> name()                { return Optional.ofNullable(name); }
    public Optional<String> cover()               { return Optional.ofNullable(cover); }
    public Optional<String> scene()               { return Optional.ofNullable(scene); }
    public Optional<Long>   createTime()          { return Optional.ofNullable(createTime); }
    public Optional<Long>   updateTime()          { return Optional.ofNullable(updateTime); }
    public Optional<String> status()              { return Optional.ofNullable(status); }
    /** Reconstruction progress in [0.0, 1.0]; present while job is running. */
    public Optional<Double> progress()            { return Optional.ofNullable(progress); }
    public Optional<WorldAssetBundle> assets()    { return Optional.ofNullable(assets); }

    @Override
    public String toString() {
        return "WorldDetail{worldId='" + worldId + "', status='" + status + "', name='" + name + "'}";
    }
}
