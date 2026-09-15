package com.chary.journalApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RidisService {

    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;

    public <T> T get(String key, Class<T> clazz) {
        if (redisTemplate == null) {
            return null;
        }
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null || !clazz.isInstance(value)) {
            return null;
        }
        return clazz.cast(value);
    }

    public void set(String key, Object value, Long timeToLiveInSeconds) {
        if (redisTemplate == null) {
            return;
        }
        redisTemplate.opsForValue().set(key, value, timeToLiveInSeconds, TimeUnit.SECONDS);
    }
}
