package com.manycoreapis.sdk.lux3d.resources;

import com.manycoreapis.sdk.core.AholoClientConfig;
import com.manycoreapis.sdk.core.AholoGatewayClient;
import com.manycoreapis.sdk.lux3d.model.PartSplitCreateParams;
import com.manycoreapis.sdk.lux3d.model.TaskListParams;
import com.manycoreapis.sdk.lux3d.model.TaskPagedList;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

final class NewApisTest {
    @Test
    void partSplitUsesExpectedEndpoint() {
        FakeGateway gateway = new FakeGateway();
        long taskId = new PartSplitResource(gateway, "/lux3d/v1")
                .create(PartSplitCreateParams.builder().glbUrl("https://example.com/model.glb").build());
        assertEquals(42L, taskId);
        assertEquals("POST", gateway.method);
        assertEquals("/lux3d/v1/part-split/task/create", gateway.path);
    }

    @Test
    void taskListMapsQueryAndResponse() {
        FakeGateway gateway = new FakeGateway();
        TaskPagedList result = new TasksResource(gateway, "/global/lux3d/v1").list(
                TaskListParams.builder().page(2).pageSize(10).status(3).startTime(100).endTime(200).build());
        assertEquals("/global/lux3d/v1/generate/task/list", gateway.path);
        assertEquals(2, gateway.query.get("page"));
        assertEquals(10, gateway.query.get("pagesize"));
        assertEquals(3, gateway.query.get("status"));
        assertEquals(100L, gateway.query.get("starttime"));
        assertEquals(200L, gateway.query.get("endtime"));
        assertEquals(1, result.total());
        assertEquals(7L, result.items().get(0).taskId());
    }

    private static final class FakeGateway extends AholoGatewayClient {
        private String method;
        private String path;
        private Map<String, Object> query;

        private FakeGateway() {
            super(AholoClientConfig.of("test-key", "cn"));
        }

        @Override
        public Map<String, Object> gatewayRequest(
                String method, String path, Map<String, Object> query, Map<String, String> headers, Object body) {
            this.method = method;
            this.path = path;
            this.query = query;
            Map<String, Object> response = new LinkedHashMap<String, Object>();
            response.put("c", "0");
            response.put("m", "");
            if (path.endsWith("/part-split/task/create")) {
                response.put("d", 42);
            } else {
                Map<String, Object> item = new LinkedHashMap<String, Object>();
                item.put("taskId", 7L);
                item.put("status", 3);
                item.put("created", 100L);
                item.put("lastModified", 200L);
                Map<String, Object> data = new LinkedHashMap<String, Object>();
                data.put("items", Arrays.<Map<String, Object>>asList(item));
                data.put("total", 1);
                data.put("page", 2);
                data.put("pageSize", 10);
                response.put("d", data);
            }
            return response;
        }
    }
}
