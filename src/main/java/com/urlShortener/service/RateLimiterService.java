package com.urlShortener.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class RateLimiterService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final int INITIAL_TOKENS = 3;
    private static final int REFILL_TOKENS = 2;
    private static final int MAX_TOKENS = 3;
    private static final int REFILL_PERIOD_SEC = 60;

    public String getBucketKey(String userId){
        return "bucket:"+userId;
    }

    public void refillTokens(String bucketKey){
        long currentTime = Instant.now().getEpochSecond();
        System.out.println("currentTime: "+currentTime);
        String lastRefillTimeKey = bucketKey + ":last_refill_time";
        String lastRefillTimeStr = redisTemplate.opsForValue().get(lastRefillTimeKey);
        Long lastRefillTime = Long.parseLong(lastRefillTimeStr);
        if(lastRefillTime == null){
            lastRefillTime = currentTime;
            redisTemplate.opsForValue().set(lastRefillTimeKey,String.valueOf(currentTime));
        }
        long timeElapsed = currentTime - lastRefillTime;
        if(timeElapsed>= REFILL_PERIOD_SEC){
            long refillCycles = timeElapsed / REFILL_PERIOD_SEC;
            Integer currentTokens = Integer.valueOf(redisTemplate.opsForValue().get(bucketKey));
            long new_tokens = Math.min(MAX_TOKENS,currentTokens+refillCycles*REFILL_TOKENS);
            redisTemplate.opsForValue().set(bucketKey,String.valueOf(new_tokens));
            redisTemplate.opsForValue().set(lastRefillTimeKey,String.valueOf(currentTime));
        }

    }

    public boolean allowRequest(String userId){
        String bucketKey = getBucketKey(userId);
        refillTokens(bucketKey);

        // Check the current token count in Redis
        String tokensLeftStr = redisTemplate.opsForValue().get(bucketKey);
        if (tokensLeftStr == null) {
            // If no tokens are available, return false
            System.out.println("No tokens available, denied.");
            return false;
        }


        Integer tokensLeft = Integer.valueOf(tokensLeftStr);
        System.out.println("Tokens Left: " + tokensLeft);

        // If there are tokens left, decrement the token count in Redis
        if (tokensLeft > 0) {
            // Atomically decrement the token count in Redis
            redisTemplate.opsForValue().decrement(bucketKey);

            System.out.println("Token decremented, remaining: " + (tokensLeft - 1));
            return true;
        }

        // If no tokens are left, deny the request
        return false;
    }


    public void initializeBucket(String userId){
        String bucketKey = getBucketKey(userId);
        System.out.println("UserId-> "+bucketKey);

        redisTemplate.opsForValue().set(bucketKey,String.valueOf(INITIAL_TOKENS));
        redisTemplate.opsForValue().set(bucketKey+ ":last_refill_time", String.valueOf(Instant.now().getEpochSecond()));
    }
}
