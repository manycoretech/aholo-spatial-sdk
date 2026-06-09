package com.manycoreapis.sdk.lux3d.resources;

import com.manycoreapis.sdk.core.BusinessException;
import com.manycoreapis.sdk.core.CmdSupport;

import java.util.Map;

/** Package-private utilities shared by Lux3D resource classes. */
final class Lux3dSupport {
    private Lux3dSupport() {}

    static long extractTaskId(Map<String, Object> body) {
        CmdSupport.assertCmdOk(body, "create task");
        Object id = body.get("d");
        if (id == null) {
            throw new BusinessException("create task succeeded but no task id",
                    String.valueOf(body.get("c")), body);
        }
        return ((Number) id).longValue();
    }
}
