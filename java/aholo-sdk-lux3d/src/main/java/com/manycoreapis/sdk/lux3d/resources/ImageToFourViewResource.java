package com.manycoreapis.sdk.lux3d.resources;

import com.manycoreapis.sdk.core.AholoGatewayClient;
import com.manycoreapis.sdk.lux3d.model.ImageToFourViewCreateParams;

import java.nio.file.Path;
import java.util.Map;

/** Single-image-to-four-view generation resource. */
public class ImageToFourViewResource {
    private final AholoGatewayClient gateway;
    private final String pathPrefix;
    public ImageToFourViewResource(AholoGatewayClient gateway, String pathPrefix) {
        this.gateway = gateway;
        this.pathPrefix = pathPrefix;
    }
    public long create(ImageToFourViewCreateParams params) {
        Map<String, Object> response = gateway.gatewayRequest(
                "POST", pathPrefix + "/generate/image-to-four-view/task/create", null, null, params);
        return Lux3dSupport.extractTaskId(response);
    }

    /**
     * Reads {@code filePath}, encodes it as a base64 data-URL, and submits a four-view task.
     * {@code params} may be null; when present, only {@code prompt} is copied.
     */
    public long createFromFile(Path filePath, ImageToFourViewCreateParams params) throws Exception {
        ImageToFourViewCreateParams.Builder builder = ImageToFourViewCreateParams.builder()
                .img(ImgTo3dResource.fileToDataUrl(filePath));
        if (params != null) {
            params.prompt().ifPresent(builder::prompt);
        }
        return create(builder.build());
    }
}
