package com.manycoreapis.sdk.examples;

import com.manycoreapis.sdk.asset.AssetClient;
import com.manycoreapis.sdk.asset.UploadResult;
import com.manycoreapis.sdk.core.AholoClientConfig;
import com.manycoreapis.sdk.world.WorldClient;
import com.manycoreapis.sdk.world.model.ReconstructionCreateParams;
import com.manycoreapis.sdk.world.model.WorldAsyncOperation;
import com.manycoreapis.sdk.world.model.WorldDetail;

import java.nio.file.Paths;

/**
 * Minimal example: upload a video, create a 3DGS reconstruction, poll until done.
 *
 * Usage:
 *   AHOLO_API_KEY=xxx mvn exec:java -pl examples \
 *     -Dexec.mainClass=com.manycoreapis.sdk.examples.WorldReconstruct \
 *     -Dexec.args="/path/to/room.mp4"
 */
public class WorldReconstruct {
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.err.println("Usage: WorldReconstruct <video-path>");
            System.exit(1);
        }

        String filePath = args[0];
        String region = System.getenv().getOrDefault("AHOLO_REGION", "cn");
        AholoClientConfig config = AholoClientConfig.ofRegion(region);
        AssetClient asset = AssetClient.create(config);
        WorldClient world = WorldClient.create(config);

        System.out.println("Uploading " + filePath + " ...");
        long t0 = System.currentTimeMillis();

        UploadResult uploaded = asset.uploadFile(Paths.get(filePath), (uploadedBytes, total) -> {
            int pct = (int) (uploadedBytes * 100 / total);
            System.out.printf("\r  %d%% (%.1f/%.1f MB)", pct,
                    uploadedBytes / 1024.0 / 1024.0, total / 1024.0 / 1024.0);
            System.out.flush();
        });
        System.out.println();
        System.out.printf("Upload complete (%dms) url=%s%n", System.currentTimeMillis() - t0, uploaded.url());

        System.out.println("Creating reconstruction task...");
        WorldAsyncOperation created = world.reconstructions().create(
                ReconstructionCreateParams.builder()
                        .name("SDK reconstruction demo")
                        .addResource(uploaded.url(), "video")
                        .taskQuality("normal")
                        .scene("model")
                        .build()
        );
        String worldId = created.worldId();
        System.out.println("worldId=" + worldId + ", polling...");

        t0 = System.currentTimeMillis();
        WorldDetail detail = world.waitFor(worldId);
        System.out.printf("Reconstruction complete (%dms)%n", System.currentTimeMillis() - t0);
        System.out.println(detail);
        detail.assets().ifPresent(assets ->
                assets.splats().ifPresent(splats ->
                        splats.urls().ifPresent(urls -> {
                            urls.plyPath().ifPresent(p -> System.out.println("PLY: " + p));
                            urls.spzPath().ifPresent(p -> System.out.println("SPZ: " + p));
                        })
                )
        );
    }
}
