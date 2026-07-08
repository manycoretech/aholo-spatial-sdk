package com.manycoreapis.sdk.examples;

import com.manycoreapis.sdk.core.AholoClientConfig;
import com.manycoreapis.sdk.lux3d.Lux3dClient;
import com.manycoreapis.sdk.lux3d.model.TaskResult;

import java.nio.file.Paths;

/**
 * Minimal example: image-to-3D with Lux3D, poll until the model URLs are ready.
 *
 * v3.0-standard (default): zip + glb + optional usdz/obj/fbx (outputs[0..4])
 * v2.0-preview:              zip + glb + usdz
 * v1.0-pro:                  single .lux3d format
 *
 * Usage:
 *   AHOLO_API_KEY=xxx mvn exec:java -pl examples \
 *     -Dexec.mainClass=com.manycoreapis.sdk.examples.Lux3dImgTo3d \
 *     -Dexec.args="/path/to/chair.png"
 */
public class Lux3dImgTo3d {
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.err.println("Usage: Lux3dImgTo3d <image-path>");
            System.exit(1);
        }

        String filePath = args[0];
        String region = System.getenv().getOrDefault("AHOLO_REGION", "cn");
        Lux3dClient lux3d = Lux3dClient.create(AholoClientConfig.ofRegion(region));

        System.out.println("Creating img-to-3D task from " + filePath + " ...");
        long taskId = lux3d.imgTo3d().createFromFile(Paths.get(filePath));
        System.out.println("taskId=" + taskId + ", polling...");

        long t0 = System.currentTimeMillis();
        TaskResult result = lux3d.tasks().waitFor(taskId);
        System.out.printf("Task complete (%dms)%n", System.currentTimeMillis() - t0);
        // v3.0-standard outputs: [zip, glb, usdz?, obj?, fbx?] — optional slots may be NOT_REQUESTED
        result.outputs().forEach(o -> o.content().ifPresent(System.out::println));
    }
}
