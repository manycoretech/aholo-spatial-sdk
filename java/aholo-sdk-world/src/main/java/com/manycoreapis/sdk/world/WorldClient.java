package com.manycoreapis.sdk.world;

import com.manycoreapis.sdk.core.AholoClientConfig;
import com.manycoreapis.sdk.core.AholoGatewayClient;
import com.manycoreapis.sdk.core.EncodingSupport;
import com.manycoreapis.sdk.core.PollSupport;
import com.manycoreapis.sdk.world.model.WorldDetail;
import com.manycoreapis.sdk.world.model.WorldListParams;
import com.manycoreapis.sdk.world.model.WorldPagedList;
import com.manycoreapis.sdk.world.resources.GenerationsResource;
import com.manycoreapis.sdk.world.resources.ReconstructionsResource;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Aholo 3DGS world API client.
 *
 * <p>Stainless-style resource access:
 * <pre>{@code
 * WorldClient world = WorldClient.create(AholoClientConfig.of("your_api_key", "com"));
 * WorldAsyncOperation op = world.reconstructions().create(
 *     ReconstructionCreateParams.builder()
 *         .addResource(url, "video")
 *         .taskQuality("normal")
 *         .scene("model")
 *         .build());
 * op = world.generations().create(GenerationCreateParams.builder().prompt("Modern living room").build());
 * WorldDetail detail = world.retrieve(op.worldId());
 * WorldPagedList page = world.list(WorldListParams.builder().pageNum(0).pageSize(20).build());
 * WorldDetail result = world.waitFor(op.worldId());
 * }</pre>
 */
public class WorldClient {
    private static final Set<String> TERMINAL_FAILURE = Collections.unmodifiableSet(
            new HashSet<String>(Arrays.asList("FAILED", "CANCELED", "TIMEOUT", "REJECTED")));

    private final AholoClientConfig config;
    private final AholoGatewayClient gateway;
    private final ReconstructionsResource reconstructions;
    private final GenerationsResource generations;

    public WorldClient(AholoClientConfig config) {
        this.config = config == null ? AholoClientConfig.defaults() : config;
        this.gateway = new AholoGatewayClient(this.config);
        String prefix = worldPathPrefix(this.config);
        this.reconstructions = new ReconstructionsResource(gateway, prefix);
        this.generations = new GenerationsResource(gateway, prefix);
    }

    public static WorldClient create(AholoClientConfig config) {
        return new WorldClient(config);
    }

    /** POST /world/v1/reconstructions */
    public ReconstructionsResource reconstructions() { return reconstructions; }

    /** POST /world/v1/generations */
    public GenerationsResource generations() { return generations; }

    /** GET /world/v1/{worldId} */
    public WorldDetail retrieve(String worldId) {
        return gateway.gatewayRequest("GET",
                worldPathPrefix(config) + "/" + EncodingSupport.urlEncode(worldId),
                null, null, null, WorldDetail.class);
    }

    /** POST /world/v1/list */
    public WorldPagedList list(WorldListParams params) {
        return gateway.gatewayRequest("POST", worldPathPrefix(config) + "/list",
                null, null, params == null ? WorldListParams.empty() : params, WorldPagedList.class);
    }

    /** Poll world detail until SUCCEEDED or a terminal failure status. */
    public WorldDetail waitFor(String worldId) throws Exception {
        return PollSupport.pollUntil(
                () -> retrieve(worldId),
                d -> "SUCCEEDED".equals(d.status().orElse(null)),
                d -> TERMINAL_FAILURE.contains(d.status().orElse(null)),
                d -> "World failed worldId=" + worldId + " status=" + d.status().orElse("unknown"),
                Duration.ofSeconds(5),
                Duration.ofHours(24)
        );
    }

    private static String worldPathPrefix(AholoClientConfig config) {
        return config.region() == AholoClientConfig.Region.COM ? "/global/world/v1" : "/world/v1";
    }

}
