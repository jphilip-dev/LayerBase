package com.jphilips.shared.spring.redis.service;


import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Set;

@Service
public class RedisHelper {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String TAG_PREFIX = "tag::";

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) // Optional: to use ISO format
            .activateDefaultTyping(
                    LaissezFaireSubTypeValidator.instance,
                    ObjectMapper.DefaultTyping.NON_FINAL,
                    JsonTypeInfo.As.PROPERTY
            );

    public RedisHelper(
            @Qualifier("sharedRedisTemplate")
            RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // GET ::
    public <T> T get(String key, Class<T> type) {
        Object raw = redisTemplate.opsForValue().get(key);
        if (raw == null) return null;
        return type.cast(raw); // works for non-generic types only
    }

    public <T> T get(String key, TypeReference<T> typeRef) {
        Object raw = redisTemplate.opsForValue().get(key);
        if (raw == null) return null;

        // Convert the raw object (LinkedHashMap, etc.) back to the target type
        return objectMapper.convertValue(raw, typeRef);
    }

    public void put(String key, Object value, Duration ttl, String... tags) {
        if (ttl != null) {
            redisTemplate.opsForValue().set(key, value, ttl);
        } else {
            redisTemplate.opsForValue().set(key, value);
        }

        for (String tag : tags) {
            addToTag(tag, key);
        }
    }

    public void put(String key, Object value, String... tags) {
        put(key, value, null, tags);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public void evictByTag(String tag) {
        String tagKey = TAG_PREFIX + tag;
        Set<Object> keys = redisTemplate.opsForSet().members(tagKey);
        if (keys != null && !keys.isEmpty()) {
            keys.forEach(k -> redisTemplate.delete((String) k));
        }
        redisTemplate.delete(tagKey);
    }


    // HELPER::
    public void addToTag(String tag, String key) {
        redisTemplate.opsForSet().add(TAG_PREFIX + tag, key);
    }

    public void addExpiryToKey(String key, Duration duration) {
        redisTemplate.expire(key, duration);
    }

    public Set<Object> getKeysByTag(String tag) {
        return redisTemplate.opsForSet().members(TAG_PREFIX + tag);
    }

}