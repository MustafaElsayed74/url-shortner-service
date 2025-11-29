package com.example.urlshortener.service;

import com.example.urlshortener.domain.dto.UrlDto;
import com.example.urlshortener.domain.entity.Url;
import org.springframework.stereotype.Service;


public interface UrlService {
    Url generateShortLink(UrlDto urlDto);
    Url persistShortUrl(Url url);
    Url getEncodedUrl(String url);
    void deleteShortLink(Url url);
}
