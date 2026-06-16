package com.manycoreapis.sdk.lux3d;

import com.manycoreapis.sdk.core.AholoClientConfig;
import com.manycoreapis.sdk.core.AholoGatewayClient;
import com.manycoreapis.sdk.lux3d.resources.ImgTo3dResource;
import com.manycoreapis.sdk.lux3d.resources.MaterialTransferResource;
import com.manycoreapis.sdk.lux3d.resources.TasksResource;
import com.manycoreapis.sdk.lux3d.resources.TextTo3dResource;

/**
 * Aholo Lux3D API client.
 *
 * <p>Stainless-style resource access:
 * <pre>{@code
 * Lux3dClient lux3d = Lux3dClient.create(AholoClientConfig.of("your_api_key", "com"));
 * long taskId = lux3d.imgTo3d().createFromFile(Paths.get("chair.png"));
 * taskId = lux3d.textTo3d().create(TextTo3dCreateParams.builder().prompt("A wooden chair").build());
 * lux3d.materialTransfer().create(MaterialTransferCreateParams.builder()...build());
 * TaskResult result = lux3d.tasks().waitFor(taskId);
 * }</pre>
 */
public class Lux3dClient {
    private final ImgTo3dResource imgTo3d;
    private final TextTo3dResource textTo3d;
    private final MaterialTransferResource materialTransfer;
    private final TasksResource tasks;

    public Lux3dClient(AholoClientConfig config) {
        AholoClientConfig cfg = config == null ? AholoClientConfig.defaults() : config;
        AholoGatewayClient gateway = new AholoGatewayClient(cfg);
        String prefix = lux3dPathPrefix(cfg);
        this.imgTo3d = new ImgTo3dResource(gateway, prefix);
        this.textTo3d = new TextTo3dResource(gateway, prefix);
        this.materialTransfer = new MaterialTransferResource(gateway, prefix);
        this.tasks = new TasksResource(gateway, prefix);
    }

    public static Lux3dClient create(AholoClientConfig config) {
        return new Lux3dClient(config);
    }

    /** Image-to-3D generation. */
    public ImgTo3dResource imgTo3d() { return imgTo3d; }

    /** Text-to-3D generation. */
    public TextTo3dResource textTo3d() { return textTo3d; }

    /** Material transfer. */
    public MaterialTransferResource materialTransfer() { return materialTransfer; }

    /** Task retrieval and polling. */
    public TasksResource tasks() { return tasks; }

    private static String lux3dPathPrefix(AholoClientConfig config) {
        return config.region() == AholoClientConfig.Region.COM ? "/global/lux3d/v1" : "/lux3d/v1";
    }
}
