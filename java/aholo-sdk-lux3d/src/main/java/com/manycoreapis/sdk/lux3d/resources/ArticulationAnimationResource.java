package com.manycoreapis.sdk.lux3d.resources;

import com.manycoreapis.sdk.core.AholoGatewayClient;
import com.manycoreapis.sdk.lux3d.model.ArticulationAnimationCreateParams;

import java.util.Map;

/** Articulation and animation resource. */
public class ArticulationAnimationResource {
    private final AholoGatewayClient gateway;
    private final String pathPrefix;
    public ArticulationAnimationResource(AholoGatewayClient gateway, String pathPrefix) {
        this.gateway = gateway;
        this.pathPrefix = pathPrefix;
    }
    public long create(ArticulationAnimationCreateParams params) {
        Map<String, Object> response = gateway.gatewayRequest(
                "POST", pathPrefix + "/articulation-animation/task/create", null, null, params);
        return Lux3dSupport.extractTaskId(response);
    }
}
