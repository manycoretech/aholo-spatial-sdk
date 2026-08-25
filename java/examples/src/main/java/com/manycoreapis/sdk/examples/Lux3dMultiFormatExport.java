package com.manycoreapis.sdk.examples;

import com.manycoreapis.sdk.core.AholoClientConfig;
import com.manycoreapis.sdk.lux3d.Lux3dClient;
import com.manycoreapis.sdk.lux3d.model.MultiFormatExportCreateParams;

import java.util.Arrays;

public final class Lux3dMultiFormatExport {
    private Lux3dMultiFormatExport() {}
    public static void main(String[] args) throws Exception {
        if (args.length < 1) throw new IllegalArgumentException("Usage: Lux3dMultiFormatExport <model-url>");
        Lux3dClient lux3d = Lux3dClient.create(AholoClientConfig.defaults());
        long taskId = lux3d.multiFormatExport().create(MultiFormatExportCreateParams.builder()
                .modelUrl(args[0]).outputFormat(Arrays.asList("usdz", "obj_zip")).build());
        System.out.println(lux3d.tasks().waitFor(taskId));
    }
}
