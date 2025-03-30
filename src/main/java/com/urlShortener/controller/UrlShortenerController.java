package com.urlShortener.controller;

import com.urlShortener.service.RateLimiterService;
import com.urlShortener.service.UrlShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UrlShortenerController {

    @Autowired
    private UrlShortenerService urlShortenerService;

    @Autowired
    private RateLimiterService limiterService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @PostMapping("/shorten")
    public String getShortenUrl(@RequestParam String longUrl, @RequestParam String userId){
        String bucketKey = limiterService.getBucketKey(userId);
        if(!redisTemplate.hasKey(bucketKey)){
            limiterService.initializeBucket(userId);
        }
        if(!limiterService.allowRequest(userId))
            return "Request Denied!!";
        return urlShortenerService.shortenUrl(longUrl);
    }

    @GetMapping("/short-code/{shortCode}")
    public String getOriginalUrl(@PathVariable String shortCode){
        Optional<String>longUrl = urlShortenerService.getLongUrl(shortCode);
        return longUrl.orElse("Url not found");
    }
}
