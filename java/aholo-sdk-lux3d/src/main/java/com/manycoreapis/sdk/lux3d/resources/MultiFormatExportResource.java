package com.manycoreapis.sdk.lux3d.resources;

import com.manycoreapis.sdk.core.AholoGatewayClient;
import com.manycoreapis.sdk.lux3d.model.MultiFormatExportCreateParams;

import java.util.Map;

/** Multi-format export resource. */
public class MultiFormatExportResource {
    private final AholoGatewayClient gateway;
    private final String pathPrefix;
    public MultiFormatExportResource(AholoGatewayClient gateway, String pathPrefix) {
        this.gateway = gateway;
        this.pathPrefix = pathPrefix;
    }
    public long create(MultiFormatExportCreateParams params) {
        Map<String, Object> response = gateway.gatewayRequest(
                "POST", pathPrefix + "/multi-format-export/task/create", null, null, params);
        return Lux3dSupport.extractTaskId(response);
    }
}
