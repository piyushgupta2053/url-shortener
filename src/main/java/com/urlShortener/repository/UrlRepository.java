package com.urlShortener.repository;

import com.urlShortener.model.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<ShortUrl,String> {
    public Optional<ShortUrl> findByShortCode(String shortCode);
}
