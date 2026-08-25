package com.manycoreapis.sdk.lux3d.resources;

import com.manycoreapis.sdk.core.AholoClientConfig;
import com.manycoreapis.sdk.core.AholoGatewayClient;
import com.manycoreapis.sdk.lux3d.model.ImageToFourViewCreateParams;
import com.manycoreapis.sdk.lux3d.model.MultiFormatExportCreateParams;
import com.manycoreapis.sdk.lux3d.model.TaskListParams;
import com.manycoreapis.sdk.lux3d.model.TaskPagedList;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

final class NewApisTest {
    @Test
    void imageToFourViewUsesExpectedEndpoint() {
        FakeGateway gateway = new FakeGateway();
        long taskId = new ImageToFourViewResource(gateway, "/lux3d/v1")
                .create(ImageToFourViewCreateParams.builder().img("https://example.com/image.png").build());
        assertEquals(42L, taskId);
        assertEquals("/lux3d/v1/generate/image-to-four-view/task/create", gateway.path);
    }

    @Test
    void multiFormatExportUsesExpectedEndpoint() {
        FakeGateway gateway = new FakeGateway();
        long taskId = new MultiFormatExportResource(gateway, "/global/lux3d/v1")
                .create(MultiFormatExportCreateParams.builder()
                        .modelUrl("https://example.com/model.glb")
                        .outputFormat(Arrays.asList("usdz"))
                        .build());
        assertEquals(42L, taskId);
        assertEquals("/global/lux3d/v1/multi-format-export/task/create", gateway.path);
    }

    @Test
    void taskListAcceptsCanceledStatus() {
        FakeGateway gateway = new FakeGateway();
        TaskPagedList result = new TasksResource(gateway, "/global/lux3d/v1").list(
                TaskListParams.builder().page(2).pageSize(10).status(6).startTime(100).endTime(200).build());
        assertEquals(6, gateway.query.get("status"));
        assertEquals(1, result.total());
    }

    @Test
    void glbExportRequiresOutputFormat() {
        assertThrows(IllegalArgumentException.class, () -> MultiFormatExportCreateParams.builder()
                .modelUrl("https://example.com/model.glb").build());
    }

    private static final class FakeGateway extends AholoGatewayClient {
        private String path;
        private Map<String, Object> query;

        private FakeGateway() { super(AholoClientConfig.of("test-key", "cn")); }

        @Override
        public Map<String, Object> gatewayRequest(
                String method, String path, Map<String, Object> query, Map<String, String> headers, Object body) {
            this.path = path;
            this.query = query;
            Map<String, Object> response = new LinkedHashMap<String, Object>();
            response.put("c", "0");
            response.put("m", "");
            response.put("f", null);
            if (method.equals("POST")) {
                response.put("d", 42);
            } else {
                Map<String, Object> item = new LinkedHashMap<String, Object>();
                item.put("taskId", 7L);
                item.put("status", 6);
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
