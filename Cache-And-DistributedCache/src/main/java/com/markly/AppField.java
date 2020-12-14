package com.markly;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author hq
 * @date 2020-11-10
 */
@Configuration(value = "spring.power")
public class AppField {

    public static String IS_USE_REDIS_CACHE = "";

    public static String IS_USE_LOCAL_CACHE = "";

    @Value("isuserediscache")
    public static void setIsUseRedisCache(String isUseRedisCache) {
        IS_USE_REDIS_CACHE = isUseRedisCache;
    }

    @Value("isuselocalcache")
    public static void setIsUseLocalCache(String isUseLocalCache) {
        IS_USE_LOCAL_CACHE = isUseLocalCache;
    }

}
