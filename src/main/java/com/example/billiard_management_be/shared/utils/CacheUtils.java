package com.example.billiard_management_be.shared.utils;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RKeys;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class CacheUtils {
    private static RedissonClient redisClient;
    private final RedissonClient tmpRedisClient;

    private static final long DEFAULT_NE_TIMEOUT = 30l;
    private static final TimeUnit DEFAULT_NE_TIME_UNIT = TimeUnit.SECONDS;

    private static final long DEFAULT_TIMEOUT = 10l;
    private static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.MINUTES;

    @PostConstruct
    public void init() {
        redisClient = tmpRedisClient;
    }

    private static RBucket getBucket(String key) {
        RBucket bucket = redisClient.getBucket(key);
        return bucket;
    }

    private static final RKeys getRKey() {
        return redisClient.getKeys();
    }

    public static <V> V get(String key) {
        RBucket<V> bucket = getBucket(key);
        return bucket.get();
    }

    public static <V> void set(String key, V value) {
        getBucket(key).set(value, DEFAULT_TIMEOUT, DEFAULT_TIME_UNIT);
    }

    public static <V> void set(String key, V value, long time, TimeUnit timeUnit) {
        getBucket(key).set(value, time, timeUnit);
    }

    public static <V> V getAndDelete(String key) {
        return (V) getBucket(key).getAndDelete();
    }

    public static Boolean setIfNotExists(String key, String value) {
        return getBucket(key).trySet(value, DEFAULT_NE_TIMEOUT, DEFAULT_NE_TIME_UNIT);
    }

    public static Boolean setIfNotExists(String key, String value, long secondsTime) {
        return getBucket(key).trySet(value, secondsTime, DEFAULT_TIME_UNIT);
    }

    public static Boolean setIfNotExists(
            String key, String value, long secondsTime, TimeUnit timeUnit) {
        return getBucket(key).trySet(value, secondsTime, timeUnit);
    }

    public static Boolean setIfNotExists(
            String key, Object value, long secondsTime, TimeUnit timeUnit) {
        return getBucket(key).trySet(value, secondsTime, timeUnit);
    }

    public static long getTtl(String key) {
        return getRKey().remainTimeToLive(key);
    }
}