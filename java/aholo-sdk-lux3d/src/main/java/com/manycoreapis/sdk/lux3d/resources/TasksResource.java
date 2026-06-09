package com.manycoreapis.sdk.lux3d.resources;

import com.manycoreapis.sdk.core.AholoGatewayClient;
import com.manycoreapis.sdk.core.BusinessException;
import com.manycoreapis.sdk.core.CmdSupport;
import com.manycoreapis.sdk.core.JsonSupport;
import com.manycoreapis.sdk.core.PollSupport;
import com.manycoreapis.sdk.lux3d.model.TaskResult;

import java.time.Duration;
import java.util.Map;

/** Lux3D task query and polling resource. */
public class TasksResource {
    private static final int STATUS_SUCCESS = 3;
    private static final int STATUS_FAILED  = 4;

    private final AholoGatewayClient gateway;
    private final String pathPrefix;

    public TasksResource(AholoGatewayClient gateway, String pathPrefix) {
        this.gateway = gateway;
        this.pathPrefix = pathPrefix;
    }

    /** GET /lux3d/v1/generate/task/get?taskid={taskId} */
    @SuppressWarnings("unchecked")
    public TaskResult retrieve(long taskId) {
        Map<String, Object> body = gateway.gatewayRequest(
                "GET",
                pathPrefix + "/generate/task/get",
                Map.of("taskid", String.valueOf(taskId)),
                null,
                null
        );
        CmdSupport.assertCmdOk(body, "get task");
        Object data = body.get("d");
        if (!(data instanceof Map<?, ?>)) {
            throw new BusinessException("Lux3D task query returned no data",
                    body.get("c") == null ? null : String.valueOf(body.get("c")), body);
        }
        Map<String, Object> dataMap = (Map<String, Object>) data;
        if (dataMap.get("taskId") == null || dataMap.get("status") == null) {
            throw new BusinessException("Lux3D task query returned incomplete data", null, dataMap);
        }
        return JsonSupport.MAPPER.convertValue(dataMap, TaskResult.class);
    }

    /** Poll task result until status is 3 (success) or 4 (failed). */
    public TaskResult waitFor(long taskId) throws Exception {
        return PollSupport.pollUntil(
                () -> retrieve(taskId),
                r -> STATUS_SUCCESS == r.status(),
                r -> STATUS_FAILED  == r.status(),
                r -> "Lux3D task failed taskId=" + taskId + " status=" + r.status(),
                Duration.ofSeconds(12),
                Duration.ofMinutes(10)
        );
    }
}
