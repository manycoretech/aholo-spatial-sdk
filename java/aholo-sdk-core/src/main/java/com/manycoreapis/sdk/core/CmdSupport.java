package com.manycoreapis.sdk.core;

import java.util.Map;

public final class CmdSupport {
    private CmdSupport() {}

    public static void assertCmdOk(Map<String, Object> body, String context) {
        Object code = body.get("c");
        if (!"0".equals(String.valueOf(code))) {
            throw new BusinessException(
                    body.get("m") == null ? context + " failed (c=" + code + ")" : String.valueOf(body.get("m")),
                    code == null ? null : String.valueOf(code),
                    body
            );
        }
    }

    public static Object assertCmdSuccess(Map<String, Object> body, String context) {
        assertCmdOk(body, context);
        Object data = body.get("d");
        if (data == null) {
            throw new BusinessException(context + " succeeded but response data is empty", String.valueOf(body.get("c")), body);
        }
        return data;
    }
}
