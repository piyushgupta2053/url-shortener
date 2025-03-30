package com.urlShortener.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
public class ShortUrl {

    @Id
    private String shortCode;
    private String longUrl;

    public ShortUrl(){

    }

    public ShortUrl(String shortCode, String longUrl) {
        this.shortCode = shortCode;
        this.longUrl = longUrl;
    }

    public String getId() {
        return shortCode;
    }

    public void setId(String id) {
        this.shortCode = id;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    @Override
    public String toString() {
        return "ShortUrl{" +
                "shortCode='" + shortCode + '\'' +
                ", longUrl='" + longUrl + '\'' +
                '}';
    }
}
