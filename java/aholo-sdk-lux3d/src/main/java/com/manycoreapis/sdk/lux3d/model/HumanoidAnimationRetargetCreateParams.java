package com.manycoreapis.sdk.lux3d.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

/** Parameters for creating a humanoid animation retarget task. */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class HumanoidAnimationRetargetCreateParams {
    @JsonProperty("rigModelUrl") private final String rigModelUrl;
    @JsonProperty("animationIds") private final List<String> animationIds;
    @JsonProperty("animationOutputMode") private final String animationOutputMode;
    @JsonProperty("outFormat") private final String outFormat;
    @JsonProperty("animateInPlace") private final Boolean animateInPlace;

    private HumanoidAnimationRetargetCreateParams(Builder builder) {
        this.rigModelUrl = builder.rigModelUrl;
        this.animationIds = builder.animationIds;
        this.animationOutputMode = builder.animationOutputMode;
        this.outFormat = builder.outFormat;
        this.animateInPlace = builder.animateInPlace;
    }
    public static Builder builder() { return new Builder(); }
    public String rigModelUrl() { return rigModelUrl; }
    public List<String> animationIds() { return animationIds; }
    public Optional<String> animationOutputMode() { return Optional.ofNullable(animationOutputMode); }
    public Optional<String> outFormat() { return Optional.ofNullable(outFormat); }
    public Optional<Boolean> animateInPlace() { return Optional.ofNullable(animateInPlace); }

    public static final class Builder {
        private String rigModelUrl;
        private List<String> animationIds;
        private String animationOutputMode;
        private String outFormat;
        private Boolean animateInPlace;
        private Builder() {}
        public Builder rigModelUrl(String rigModelUrl) { this.rigModelUrl = rigModelUrl; return this; }
        public Builder animationIds(List<String> animationIds) { this.animationIds = animationIds; return this; }
        public Builder animationOutputMode(String animationOutputMode) {
            this.animationOutputMode = animationOutputMode;
            return this;
        }
        public Builder outFormat(String outFormat) { this.outFormat = outFormat; return this; }
        public Builder animateInPlace(Boolean animateInPlace) { this.animateInPlace = animateInPlace; return this; }
        public HumanoidAnimationRetargetCreateParams build() {
            if (rigModelUrl == null || rigModelUrl.trim().isEmpty()) {
                throw new IllegalArgumentException("rigModelUrl is required");
            }
            if (animationIds == null || animationIds.size() < 1 || animationIds.size() > 10
                    || new HashSet<String>(animationIds).size() != animationIds.size()
                    || !HumanoidAnimationIds.ALL.containsAll(animationIds)) {
                throw new IllegalArgumentException("animationIds must be 1 to 10 unique humanoid animation ids");
            }
            if (animationOutputMode != null
                    && !("separate".equals(animationOutputMode) || "combined".equals(animationOutputMode))) {
                throw new IllegalArgumentException("animationOutputMode must be separate or combined");
            }
            if (outFormat != null && !("glb".equals(outFormat) || "fbx".equals(outFormat))) {
                throw new IllegalArgumentException("outFormat must be glb or fbx");
            }
            animationIds = Collections.unmodifiableList(new ArrayList<String>(animationIds));
            return new HumanoidAnimationRetargetCreateParams(this);
        }
    }
}
