package com.example.urlshortener.repository;

import com.example.urlshortener.domain.entity.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UrlRepository extends JpaRepository<Url, UUID> {
    Url findByShortUrl(String shortLink);
}
