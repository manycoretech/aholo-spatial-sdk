package com.manycoreapis.sdk.lux3d.resources;

import com.manycoreapis.sdk.core.AholoClientConfig;
import com.manycoreapis.sdk.core.AholoGatewayClient;
import com.manycoreapis.sdk.lux3d.model.ArticulationAnimationCreateParams;
import com.manycoreapis.sdk.lux3d.model.HumanoidAnimationRetargetCreateParams;
import com.manycoreapis.sdk.lux3d.model.HumanoidAutoRigCreateParams;
import com.manycoreapis.sdk.lux3d.model.ImageToFourViewCreateParams;
import com.manycoreapis.sdk.lux3d.model.MultimodalToImageCreateParams;
import com.manycoreapis.sdk.lux3d.model.MultiFormatExportCreateParams;
import com.manycoreapis.sdk.lux3d.model.PartSplitCreateParams;
import com.manycoreapis.sdk.lux3d.model.TaskListParams;
import com.manycoreapis.sdk.lux3d.model.TaskPagedList;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class NewApisTest {
    @Test
    void imageToFourViewUsesExpectedEndpoint() {
        FakeGateway gateway = new FakeGateway();
        long taskId = new ImageToFourViewResource(gateway, "/lux3d/v1")
                .create(ImageToFourViewCreateParams.builder().img("https://example.com/image.png").build());
        assertEquals(42L, taskId);
        assertEquals("/lux3d/v1/generate/image-to-four-view/task/create", gateway.path);
        ImageToFourViewCreateParams body = (ImageToFourViewCreateParams) gateway.body;
        assertEquals("https://example.com/image.png", body.img().get());
    }

    @Test
    void imageToFourViewAcceptsPromptOnly() {
        FakeGateway gateway = new FakeGateway();
        long taskId = new ImageToFourViewResource(gateway, "/lux3d/v1")
                .create(ImageToFourViewCreateParams.builder().prompt("a wooden dining chair").build());
        assertEquals(42L, taskId);
        assertEquals("/lux3d/v1/generate/image-to-four-view/task/create", gateway.path);
        ImageToFourViewCreateParams body = (ImageToFourViewCreateParams) gateway.body;
        assertEquals("a wooden dining chair", body.prompt().get());
        assertEquals(false, body.img().isPresent());
        new ImageToFourViewResource(gateway, "/lux3d/v1").create(
                ImageToFourViewCreateParams.builder()
                        .img("https://example.com/image.png")
                        .prompt("keep the viewpoint")
                        .build());
        ImageToFourViewCreateParams combined = (ImageToFourViewCreateParams) gateway.body;
        assertEquals("https://example.com/image.png", combined.img().get());
        assertEquals("keep the viewpoint", combined.prompt().get());
    }

    @Test
    void imageToFourViewCreateFromFileEncodesLocalImage() throws Exception {
        Path file = Files.createTempFile("four-view", ".png");
        try {
            Files.write(file, "png".getBytes(StandardCharsets.UTF_8));
            FakeGateway gateway = new FakeGateway();
            long taskId = new ImageToFourViewResource(gateway, "/lux3d/v1").createFromFile(
                    file,
                    ImageToFourViewCreateParams.builder().prompt("keep the viewpoint").build());
            assertEquals(42L, taskId);
            ImageToFourViewCreateParams body = (ImageToFourViewCreateParams) gateway.body;
            assertTrue(body.img().get().startsWith("data:"));
            assertEquals("keep the viewpoint", body.prompt().get());
        } finally {
            Files.deleteIfExists(file);
        }
    }

    @Test
    void multimodalToImagePostsPromptOnlyToChinaEndpoint() {
        FakeGateway gateway = new FakeGateway();
        long taskId = new MultimodalToImageResource(gateway, "/lux3d/v1")
                .create(MultimodalToImageCreateParams.builder().prompt("a wooden dining chair").build());
        assertEquals(42L, taskId);
        assertEquals("/lux3d/v1/generate/multimodal-to-image/task/create", gateway.path);
        MultimodalToImageCreateParams body = (MultimodalToImageCreateParams) gateway.body;
        assertEquals("a wooden dining chair", body.prompt().get());
        assertEquals(false, body.img().isPresent());
    }

    @Test
    void multimodalToImageRejectsEmptyBody() {
        assertThrows(IllegalArgumentException.class, () -> MultimodalToImageCreateParams.builder().build());
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
    void multiFormatExportAcceptsStlAnd3mf() {
        FakeGateway gateway = new FakeGateway();
        long taskId = new MultiFormatExportResource(gateway, "/lux3d/v1")
                .create(MultiFormatExportCreateParams.builder()
                        .modelUrl("https://example.com/model.glb")
                        .outputFormat(Arrays.asList("stl", "3mf"))
                        .build());
        assertEquals(42L, taskId);
    }

    @Test
    void taskListMapsFiltersWithoutCanceledStatus() {
        FakeGateway gateway = new FakeGateway();
        TaskPagedList result = new TasksResource(gateway, "/global/lux3d/v1").list(
                TaskListParams.builder().page(2).pageSize(10).status(3).startTime(100).endTime(200).build());
        assertEquals(3, gateway.query.get("status"));
        assertEquals(1, result.total());
    }

    @Test
    void glbExportRequiresOutputFormat() {
        assertThrows(IllegalArgumentException.class, () -> MultiFormatExportCreateParams.builder()
                .modelUrl("https://example.com/model.glb").build());
    }

    @Test
    void partSplitArticulationRigAndRetargetUseExpectedEndpoints() {
        FakeGateway gateway = new FakeGateway();
        assertEquals(42L, new PartSplitResource(gateway, "/lux3d/v1").create(
                PartSplitCreateParams.builder().glbUrl("https://example.com/model.glb").build()));
        assertEquals("/lux3d/v1/part-split/task/create", gateway.path);
        assertEquals(42L, new ArticulationAnimationResource(gateway, "/global/lux3d/v1").create(
                ArticulationAnimationCreateParams.builder()
                        .glbUrl("https://example.com/model.glb")
                        .prompt("open the lid")
                        .build()));
        assertEquals("/global/lux3d/v1/articulation-animation/task/create", gateway.path);
        assertEquals(42L, new HumanoidAutoRigResource(gateway, "/lux3d/v1").create(
                HumanoidAutoRigCreateParams.builder().modelUrl("https://example.com/model.glb").build()));
        assertEquals("/lux3d/v1/animations/rig/task/create", gateway.path);
        assertEquals(42L, new HumanoidAnimationRetargetResource(gateway, "/lux3d/v1").create(
                HumanoidAnimationRetargetCreateParams.builder()
                        .rigModelUrl("https://example.com/rig.glb")
                        .animationIds(Arrays.asList("Idle_Loop", "Walk_Loop"))
                        .outFormat("fbx")
                        .build()));
        assertEquals("/lux3d/v1/animations/retarget/task/create", gateway.path);
        assertThrows(IllegalArgumentException.class, () -> HumanoidAnimationRetargetCreateParams.builder()
                .rigModelUrl("https://example.com/rig.glb")
                .animationIds(Arrays.asList("NotAnAction"))
                .build());
    }

    private static final class FakeGateway extends AholoGatewayClient {
        private String path;
        private Map<String, Object> query;
        private Object body;

        private FakeGateway() { super(AholoClientConfig.of("test-key", "cn")); }

        @Override
        public Map<String, Object> gatewayRequest(
                String method, String path, Map<String, Object> query, Map<String, String> headers, Object body) {
            this.path = path;
            this.query = query;
            this.body = body;
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
