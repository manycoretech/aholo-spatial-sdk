package com.manycoreapis.sdk.lux3d.resources;

import com.manycoreapis.sdk.core.AholoGatewayClient;
import com.manycoreapis.sdk.lux3d.model.PartSplitCreateParams;

import java.util.Map;

/** GLB part-split resource. */
public class PartSplitResource {
    private final AholoGatewayClient gateway;
    private final String pathPrefix;

    public PartSplitResource(AholoGatewayClient gateway, String pathPrefix) {
        this.gateway = gateway;
        this.pathPrefix = pathPrefix;
    }

    /** POST /lux3d/v1/part-split/task/create */
    public long create(PartSplitCreateParams params) {
        Map<String, Object> response = gateway.gatewayRequest(
                "POST", pathPrefix + "/part-split/task/create", null, null, params);
        return Lux3dSupport.extractTaskId(response);
    }
}
