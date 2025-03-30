package com.urlShortener.service;

import com.urlShortener.model.ShortUrl;
import com.urlShortener.repository.UrlRepository;
import com.urlShortener.util.Base62Encoder;
import com.urlShortener.util.SnowflakeIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UrlShortenerService {

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private UrlCacheService cacheService;

    public String shortenUrl(String longUrl){
        long id = SnowflakeIdGenerator.generateId();
        System.out.println("snowflake Id: "+id);
        String shortCode = Base62Encoder.generateShortCode(id);

        ShortUrl shortUrl =  new ShortUrl(shortCode,longUrl);
        urlRepository.save(shortUrl);
        cacheService.saveToCache(shortCode,longUrl);
        System.out.println("shortCode: "+shortCode);
        return shortCode;
    }

    public Optional<String> getLongUrl(String shortCode) {

        String cachedUrl = cacheService.getFromCache(shortCode);
        if(cachedUrl!=null){
            return Optional.of(cachedUrl);
        }
        Optional<ShortUrl> shortUrl = urlRepository.findByShortCode(shortCode);
        System.out.println("longUrl: "+shortUrl);
        return shortUrl.map(ShortUrl::getLongUrl);
    }
}
