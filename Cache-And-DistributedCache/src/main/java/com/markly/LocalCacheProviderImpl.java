package com.markly;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

/**
 * @author hq
 * @date 2020-11-10
 */
@Configuration
@ComponentScan(basePackages = AppConst.BASE_PACKAGE_NAME)
@Qualifier("localCacheService")
public class LocalCacheProviderImpl implements CacheProviderService {

    private static final Map<String, Cache<String, Object>> CACHE_MAP = Maps.newConcurrentMap();

    private final Lock lock = new ReentrantLock();

    static {
        Cache<String, Object> cacheContainer = CacheBuilder.newBuilder()
                .maximumSize(AppConst.CACHE_MAXIMUM_SIZE)
                //最后一次写入后的一段时间移出
                .expireAfterWrite(AppConst.CACHE_MINUTE, TimeUnit.MILLISECONDS)
                //最后一次访问后的一段时间移出
                //.expireAfterAccess(AppConst.CACHE_MINUTE, TimeUnit.MILLISECONDS)
                .recordStats()//开启统计功能
                .build();

        CACHE_MAP.put(String.valueOf(AppConst.CACHE_MINUTE), cacheContainer);
    }


    @Override
    public <T> T get(String key) {
        return get(key, null, null, AppConst.CACHE_MINUTE);
    }

    @Override
    public <T> T get(String key, Function<String, T> function) {
        return get(key, function, key, AppConst.CACHE_MINUTE);
    }

    @Override
    public <T, M> T get(String key, Function<M, T> function, M funcParm) {
        return get(key, function, funcParm, AppConst.CACHE_MINUTE);
    }

    @Override
    public <T> T get(String key, Function<String, T> function, Long expireTime) {
        return get(key, function, key, expireTime);
    }

    @Override
    public <T, M> T get(String key, Function<M, T> function, M funcParm, Long expireTime) {
        T obj = null;
        if (StringUtils.isEmpty(key)) {
            return obj;
        }
        Cache<String, Object> cacheContainer = getCacheContainer(getExpireTime(expireTime));

        try {
            if (function == null) {
                obj = (T) cacheContainer.getIfPresent(key);
            } else {
                final Long cachedTime = expireTime;
                obj = (T) cacheContainer.get(key, () -> {
                    T retObj = function.apply(funcParm);
                    return retObj;
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return obj;
    }

    @Override
    public <T> void set(String key, T obj) {
        set(key, obj, AppConst.CACHE_MINUTE);
    }

    @Override
    public <T extends Object> void set(String key, T obj, Long expireTime) {
        if (StringUtils.isEmpty(key) || obj == null) {
            return;
        }

        Cache<String, Object> cacheContainer = getCacheContainer(getExpireTime(expireTime));
        cacheContainer.put(key, obj);
    }

    @Override
    public void remove(String key) {
        if (StringUtils.isEmpty(key)) {
            return;
        }

        Cache<String, Object> cacheContainer = getCacheContainer(getExpireTime(AppConst.CACHE_MINUTE));
        cacheContainer.invalidate(key);
    }

    @Override
    public boolean contains(String key) {
        boolean exists = false;
        if (StringUtils.isEmpty(key)) {
            return false;
        }

        Object obj = get(key);
        if (obj != null) {
            exists = true;
        }

        return exists;
    }

    private Cache<String, Object> getCacheContainer(Long expireTime) {
        Cache<String, Object> cacheContainer = null;
        if (expireTime == null) {
            return cacheContainer;
        }

        String mapKey = String.valueOf(expireTime);
        if (CACHE_MAP.containsKey(mapKey)) {
            cacheContainer = CACHE_MAP.get(mapKey);
            return cacheContainer;
        }

        lock.lock();
        try {
            cacheContainer = CacheBuilder.newBuilder()
                    .maximumSize(AppConst.CACHE_MAXIMUM_SIZE)
                    .expireAfterWrite(expireTime, TimeUnit.MILLISECONDS)
                    .recordStats()//开启统计功能
                    .build();

            CACHE_MAP.put(mapKey, cacheContainer);

        } finally {
            lock.unlock();
        }

        return cacheContainer;
    }

    /**
     * 获取过期时间 单位：毫秒
     *
     * @param expireTime 传人的过期时间 单位毫秒 如小于1分钟，默认为10分钟
     **/
    private Long getExpireTime(Long expireTime) {
        Long result = expireTime;
        if (expireTime == null || expireTime < AppConst.CACHE_MINUTE / 10) {
            result = AppConst.CACHE_MINUTE;
        }

        return result;
    }


}
