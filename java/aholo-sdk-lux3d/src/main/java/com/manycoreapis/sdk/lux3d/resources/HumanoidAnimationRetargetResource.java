package com.manycoreapis.sdk.lux3d.resources;

import com.manycoreapis.sdk.core.AholoGatewayClient;
import com.manycoreapis.sdk.lux3d.model.HumanoidAnimationRetargetCreateParams;

import java.util.Map;

/** Humanoid animation retarget resource. */
public class HumanoidAnimationRetargetResource {
    private final AholoGatewayClient gateway;
    private final String pathPrefix;
    public HumanoidAnimationRetargetResource(AholoGatewayClient gateway, String pathPrefix) {
        this.gateway = gateway;
        this.pathPrefix = pathPrefix;
    }
    public long create(HumanoidAnimationRetargetCreateParams params) {
        Map<String, Object> response = gateway.gatewayRequest(
                "POST", pathPrefix + "/animations/retarget/task/create", null, null, params);
        return Lux3dSupport.extractTaskId(response);
    }
}
