package com.quantil.cm.api.framework.util;

import java.util.UUID;

public class UuidUtil {
    private UuidUtil(){
    }

    public static String randomUUID(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replaceAll("-", "");
    }
}
