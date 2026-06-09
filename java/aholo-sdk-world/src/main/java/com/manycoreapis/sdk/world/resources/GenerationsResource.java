package com.manycoreapis.sdk.world.resources;

import com.manycoreapis.sdk.core.AholoGatewayClient;
import com.manycoreapis.sdk.world.model.GenerationCreateParams;
import com.manycoreapis.sdk.world.model.WorldAsyncOperation;

/** POST /world/v1/generations — 3DGS generation jobs. */
public class GenerationsResource {
    private final AholoGatewayClient gateway;
    private final String pathPrefix;

    public GenerationsResource(AholoGatewayClient gateway, String pathPrefix) {
        this.gateway = gateway;
        this.pathPrefix = pathPrefix;
    }

    public WorldAsyncOperation create(GenerationCreateParams params) {
        return gateway.gatewayRequest("POST", pathPrefix + "/generations",
                null, null, params, WorldAsyncOperation.class);
    }
}
