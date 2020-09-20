package org.withyu.util;

import java.util.UUID;

public class CodeUtil {
    /**
     * 生成唯一的激活码
     *
     * @return
     */
    public static String generateUniqueCode() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
