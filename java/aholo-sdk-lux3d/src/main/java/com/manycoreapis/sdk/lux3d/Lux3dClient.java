package com.manycoreapis.sdk.lux3d;

import com.manycoreapis.sdk.core.AholoClientConfig;
import com.manycoreapis.sdk.core.AholoGatewayClient;
import com.manycoreapis.sdk.lux3d.resources.ArticulationAnimationResource;
import com.manycoreapis.sdk.lux3d.resources.HumanoidAnimationRetargetResource;
import com.manycoreapis.sdk.lux3d.resources.HumanoidAutoRigResource;
import com.manycoreapis.sdk.lux3d.resources.ImageToFourViewResource;
import com.manycoreapis.sdk.lux3d.resources.ImgTo3dResource;
import com.manycoreapis.sdk.lux3d.resources.MaterialTransferResource;
import com.manycoreapis.sdk.lux3d.resources.MultimodalToImageResource;
import com.manycoreapis.sdk.lux3d.resources.MultiFormatExportResource;
import com.manycoreapis.sdk.lux3d.resources.PartSplitResource;
import com.manycoreapis.sdk.lux3d.resources.TasksResource;
import com.manycoreapis.sdk.lux3d.resources.TextTo3dResource;

/** Aholo Lux3D API client. */
public class Lux3dClient {
    private final MultimodalToImageResource multimodalToImage;
    private final ImageToFourViewResource imageToFourView;
    private final ImgTo3dResource imgTo3d;
    private final TextTo3dResource textTo3d;
    private final MaterialTransferResource materialTransfer;
    private final MultiFormatExportResource multiFormatExport;
    private final PartSplitResource partSplit;
    private final ArticulationAnimationResource articulationAnimation;
    private final HumanoidAutoRigResource humanoidAutoRig;
    private final HumanoidAnimationRetargetResource humanoidAnimationRetarget;
    private final TasksResource tasks;

    public Lux3dClient(AholoClientConfig config) {
        AholoClientConfig cfg = config == null ? AholoClientConfig.defaults() : config;
        AholoGatewayClient gateway = new AholoGatewayClient(cfg);
        String prefix = lux3dPathPrefix(cfg);
        this.multimodalToImage = new MultimodalToImageResource(gateway, prefix);
        this.imageToFourView = new ImageToFourViewResource(gateway, prefix);
        this.imgTo3d = new ImgTo3dResource(gateway, prefix);
        this.textTo3d = new TextTo3dResource(gateway, prefix);
        this.materialTransfer = new MaterialTransferResource(gateway, prefix);
        this.multiFormatExport = new MultiFormatExportResource(gateway, prefix);
        this.partSplit = new PartSplitResource(gateway, prefix);
        this.articulationAnimation = new ArticulationAnimationResource(gateway, prefix);
        this.humanoidAutoRig = new HumanoidAutoRigResource(gateway, prefix);
        this.humanoidAnimationRetarget = new HumanoidAnimationRetargetResource(gateway, prefix);
        this.tasks = new TasksResource(gateway, prefix);
    }

    public static Lux3dClient create(AholoClientConfig config) { return new Lux3dClient(config); }
    public MultimodalToImageResource multimodalToImage() { return multimodalToImage; }
    public ImageToFourViewResource imageToFourView() { return imageToFourView; }
    public ImgTo3dResource imgTo3d() { return imgTo3d; }
    public TextTo3dResource textTo3d() { return textTo3d; }
    public MaterialTransferResource materialTransfer() { return materialTransfer; }
    public MultiFormatExportResource multiFormatExport() { return multiFormatExport; }
    public PartSplitResource partSplit() { return partSplit; }
    public ArticulationAnimationResource articulationAnimation() { return articulationAnimation; }
    public HumanoidAutoRigResource humanoidAutoRig() { return humanoidAutoRig; }
    public HumanoidAnimationRetargetResource humanoidAnimationRetarget() { return humanoidAnimationRetarget; }
    public TasksResource tasks() { return tasks; }

    private static String lux3dPathPrefix(AholoClientConfig config) {
        return config.region() == AholoClientConfig.Region.COM ? "/global/lux3d/v1" : "/lux3d/v1";
    }
}
