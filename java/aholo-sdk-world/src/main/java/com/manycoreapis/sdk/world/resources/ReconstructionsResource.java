package com.manycoreapis.sdk.world.resources;

import com.manycoreapis.sdk.core.AholoGatewayClient;
import com.manycoreapis.sdk.world.model.ReconstructionCreateParams;
import com.manycoreapis.sdk.world.model.WorldAsyncOperation;

/** POST /world/v1/reconstructions — 3DGS reconstruction jobs. */
public class ReconstructionsResource {
    private final AholoGatewayClient gateway;
    private final String pathPrefix;

    public ReconstructionsResource(AholoGatewayClient gateway, String pathPrefix) {
        this.gateway = gateway;
        this.pathPrefix = pathPrefix;
    }

    public WorldAsyncOperation create(ReconstructionCreateParams params) {
        return gateway.gatewayRequest("POST", pathPrefix + "/reconstructions",
                null, null, params, WorldAsyncOperation.class);
    }
}
