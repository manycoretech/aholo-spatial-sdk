package com.manycoreapis.sdk.examples;

import com.manycoreapis.sdk.core.AholoClientConfig;
import com.manycoreapis.sdk.lux3d.Lux3dClient;
import com.manycoreapis.sdk.lux3d.model.PartSplitCreateParams;
import com.manycoreapis.sdk.lux3d.model.TaskListParams;

public final class Lux3dPartSplitList {
    private Lux3dPartSplitList() {}

    public static void main(String[] args) {
        if (args.length < 1) throw new IllegalArgumentException("Usage: Lux3dPartSplitList <glb-url>");
        Lux3dClient lux3d = Lux3dClient.create(AholoClientConfig.ofRegion("cn"));
        long taskId = lux3d.partSplit().create(PartSplitCreateParams.builder().glbUrl(args[0]).build());
        System.out.println("partSplit taskId=" + taskId);
        System.out.println("recent total=" + lux3d.tasks().list(
                TaskListParams.builder().page(1).pageSize(20).build()).total());
    }
}
