package com.urlShortener.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class UrlCacheService {
    private final StringRedisTemplate redisTemplate;


    public UrlCacheService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    public void saveToCache(String shortCode,String longUrl){
        redisTemplate.opsForValue().set(shortCode,longUrl,24, TimeUnit.MINUTES);
    }

    public String getFromCache(String shortCode){
       return redisTemplate.opsForValue().get(shortCode);
    }
}
