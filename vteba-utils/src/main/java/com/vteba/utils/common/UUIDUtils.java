package com.vteba.utils.common;

import java.util.UUID;

/**
 * 简单的随机数UUID
 * @author yinlei 
 * @since 2013-12-17
 */
public class UUIDUtils {
    
    public static String uuid() {
        return UUID.randomUUID().toString();
    }
}
