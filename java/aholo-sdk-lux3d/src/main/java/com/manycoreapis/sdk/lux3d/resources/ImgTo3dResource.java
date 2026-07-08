package com.manycoreapis.sdk.lux3d.resources;

import com.manycoreapis.sdk.core.AholoGatewayClient;
import com.manycoreapis.sdk.lux3d.model.ImgTo3dCreateParams;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Map;

/** Image-to-3D generation resource. */
public class ImgTo3dResource {
    private final AholoGatewayClient gateway;
    private final String pathPrefix;

    public ImgTo3dResource(AholoGatewayClient gateway, String pathPrefix) {
        this.gateway = gateway;
        this.pathPrefix = pathPrefix;
    }

    public long create(ImgTo3dCreateParams params) {
        Map<String, Object> response = gateway.gatewayRequest(
                "POST", pathPrefix + "/generate/img-to-3d/task/create", null, null, params);
        return Lux3dSupport.extractTaskId(response);
    }

    /** Reads {@code filePath}, encodes it as a base64 data-URL, and submits an img-to-3D task. */
    public long createFromFile(Path filePath) throws Exception {
        return createFromFile(filePath, null);
    }

    /**
     * Reads {@code filePath}, encodes it as a base64 data-URL, and submits an img-to-3D task.
     * Extra parameters (e.g. {@code version}) are taken from {@code params}; pass {@code null}
     * to use defaults.
     */
    public long createFromFile(Path filePath, ImgTo3dCreateParams params) throws Exception {
        String dataUrl = fileToDataUrl(filePath);
        ImgTo3dCreateParams.Builder builder = ImgTo3dCreateParams.builder().img(dataUrl);
        if (params != null) {
            params.version().ifPresent(builder::version);
            params.faceCount().ifPresent(builder::faceCount);
            params.needUsdz().ifPresent(builder::needUsdz);
            params.needObj().ifPresent(builder::needObj);
            params.needFbx().ifPresent(builder::needFbx);
        }
        return create(builder.build());
    }

    public static String fileToDataUrl(Path filePath) throws Exception {
        byte[] data = Files.readAllBytes(filePath);
        String mime = Files.probeContentType(filePath);
        if (mime == null) mime = "application/octet-stream";
        return "data:" + mime + ";base64," + Base64.getEncoder().encodeToString(data);
    }
}
