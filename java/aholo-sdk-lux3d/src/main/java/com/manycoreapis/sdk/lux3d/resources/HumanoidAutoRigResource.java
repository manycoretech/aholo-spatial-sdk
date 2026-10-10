package com.manycoreapis.sdk.lux3d.resources;

import com.manycoreapis.sdk.core.AholoGatewayClient;
import com.manycoreapis.sdk.lux3d.model.HumanoidAutoRigCreateParams;

import java.util.Map;

/** Humanoid auto-rig resource. */
public class HumanoidAutoRigResource {
    private final AholoGatewayClient gateway;
    private final String pathPrefix;
    public HumanoidAutoRigResource(AholoGatewayClient gateway, String pathPrefix) {
        this.gateway = gateway;
        this.pathPrefix = pathPrefix;
    }
    public long create(HumanoidAutoRigCreateParams params) {
        Map<String, Object> response = gateway.gatewayRequest(
                "POST", pathPrefix + "/animations/rig/task/create", null, null, params);
        return Lux3dSupport.extractTaskId(response);
    }
}
