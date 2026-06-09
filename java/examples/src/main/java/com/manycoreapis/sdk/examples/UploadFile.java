package com.manycoreapis.sdk.examples;

import com.manycoreapis.sdk.asset.AssetClient;
import com.manycoreapis.sdk.asset.UploadResult;
import com.manycoreapis.sdk.core.AholoClientConfig;

import java.nio.file.Paths;

/**
 * Minimal example: upload a local file and print the public URL.
 *
 * Usage:
 *   AHOLO_API_KEY=xxx mvn exec:java -pl examples \
 *     -Dexec.mainClass=com.manycoreapis.sdk.examples.UploadFile \
 *     -Dexec.args="/path/to/photo.jpg"
 */
public class UploadFile {
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.err.println("Usage: UploadFile <file-path>");
            System.exit(1);
        }

        String filePath = args[0];
        String region = System.getenv().getOrDefault("AHOLO_REGION", "cn");
        AssetClient asset = AssetClient.create(AholoClientConfig.ofRegion(region));

        System.out.println("Uploading " + filePath + " ...");
        long t0 = System.currentTimeMillis();

        UploadResult result = asset.uploadFile(Paths.get(filePath), (uploaded, total) -> {
            int pct = (int) (uploaded * 100 / total);
            System.out.printf("\r  %d%% (%.1f/%.1f MB)", pct,
                    uploaded / 1024.0 / 1024.0, total / 1024.0 / 1024.0);
            System.out.flush();
        });
        System.out.println();

        System.out.printf("Upload complete (%dms)%n", System.currentTimeMillis() - t0);
        System.out.println("url: " + result.url());
    }
}
