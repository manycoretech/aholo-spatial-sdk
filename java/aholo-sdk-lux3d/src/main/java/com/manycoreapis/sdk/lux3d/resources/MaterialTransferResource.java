package com.manycoreapis.sdk.lux3d.resources;

import com.manycoreapis.sdk.core.AholoGatewayClient;
import com.manycoreapis.sdk.lux3d.model.MaterialTransferCreateParams;

import java.util.Map;

/** Material transfer resource. */
public class MaterialTransferResource {
    private final AholoGatewayClient gateway;
    private final String pathPrefix;

    public MaterialTransferResource(AholoGatewayClient gateway, String pathPrefix) {
        this.gateway = gateway;
        this.pathPrefix = pathPrefix;
    }

    public long create(MaterialTransferCreateParams params) {
        Map<String, Object> response = gateway.gatewayRequest(
                "POST", pathPrefix + "/generate/material-transfer/task/create", null, null, params);
        return Lux3dSupport.extractTaskId(response);
    }
}
