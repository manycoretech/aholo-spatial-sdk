package com.manycoreapis.sdk.lux3d.resources;

import com.manycoreapis.sdk.core.AholoGatewayClient;
import com.manycoreapis.sdk.lux3d.model.MultimodalToImageCreateParams;

import java.nio.file.Path;
import java.util.Map;

/** Multimodal single-image generation resource. */
public class MultimodalToImageResource {
    private final AholoGatewayClient gateway;
    private final String pathPrefix;
    public MultimodalToImageResource(AholoGatewayClient gateway, String pathPrefix) {
        this.gateway = gateway;
        this.pathPrefix = pathPrefix;
    }
    public long create(MultimodalToImageCreateParams params) {
        Map<String, Object> response = gateway.gatewayRequest(
                "POST", pathPrefix + "/generate/multimodal-to-image/task/create", null, null, params);
        return Lux3dSupport.extractTaskId(response);
    }

    /**
     * Reads {@code filePath}, encodes it as a base64 data-URL, and submits a multimodal-to-image task.
     * {@code params} may be null; when present, only {@code prompt} is copied.
     */
    public long createFromFile(Path filePath, MultimodalToImageCreateParams params) throws Exception {
        MultimodalToImageCreateParams.Builder builder = MultimodalToImageCreateParams.builder()
                .img(ImgTo3dResource.fileToDataUrl(filePath));
        if (params != null) {
            params.prompt().ifPresent(builder::prompt);
        }
        return create(builder.build());
    }
}
