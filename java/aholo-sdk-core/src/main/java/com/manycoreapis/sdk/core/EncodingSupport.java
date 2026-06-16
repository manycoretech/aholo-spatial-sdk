package com.manycoreapis.sdk.core;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/** Java 8 compatible encoding helpers shared by HTTP clients. */
public final class EncodingSupport {
    private EncodingSupport() {}

    public static String urlEncode(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("UTF-8 not supported", e);
        }
    }

    public static boolean isBlank(String value) {
        if (value == null) {
            return true;
        }
        for (int i = 0; i < value.length(); i++) {
            if (!Character.isWhitespace(value.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
