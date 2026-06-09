package com.manycoreapis.sdk.lux3d.resources;

import com.manycoreapis.sdk.core.AholoGatewayClient;
import com.manycoreapis.sdk.lux3d.model.TextTo3dCreateParams;

import java.util.Map;

/** Text-to-3D generation resource. */
public class TextTo3dResource {
    private final AholoGatewayClient gateway;
    private final String pathPrefix;

    public TextTo3dResource(AholoGatewayClient gateway, String pathPrefix) {
        this.gateway = gateway;
        this.pathPrefix = pathPrefix;
    }

    public long create(TextTo3dCreateParams params) {
        Map<String, Object> response = gateway.gatewayRequest(
                "POST", pathPrefix + "/generate/text-to-3d/task/create", null, null, params);
        return Lux3dSupport.extractTaskId(response);
    }
}
