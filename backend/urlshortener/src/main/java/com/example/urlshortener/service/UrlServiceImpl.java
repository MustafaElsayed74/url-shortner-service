package com.example.urlshortener.service;

import com.example.urlshortener.domain.dto.UrlDto;
import com.example.urlshortener.domain.entity.Url;
import com.example.urlshortener.repository.UrlRepository;
import com.google.common.hash.Hashing;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UrlServiceImpl implements UrlService {

    private final UrlRepository urlRepository;

    @Override
    public Url generateShortLink(UrlDto urlDto) {
        if (StringUtils.isNotEmpty(urlDto.getUrl())) {
            String encodedUrl = encodedUrl(urlDto.getUrl());

            Url urlToPersist = new Url();

            urlToPersist.setOriginalUrl(urlDto.getUrl());
            urlToPersist.setShortUrl(encodedUrl);
            urlToPersist.setCreatedAt(LocalDateTime.now());
            urlToPersist.setExpiresAt(getExpirationDate(urlDto.getExpiresAt(), urlToPersist.getCreatedAt()));

            Url urlToReturn = persistShortUrl(urlToPersist);

            if(urlToReturn != null) return  urlToReturn;
            else return null;

        }

        return null;

    }

    private LocalDateTime getExpirationDate(String expiresAt, LocalDateTime createdAt) {

        if (StringUtils.isBlank(expiresAt)) {
            return createdAt.plusSeconds(60);
        }

        LocalDateTime expirationDateToReturn = LocalDateTime.parse(expiresAt);

        return expirationDateToReturn;
    }


    private String encodedUrl(String url) {
        String encodedUrl = "";
        LocalDateTime time = LocalDateTime.now();

        encodedUrl = Hashing.murmur3_32()
                .hashString(url.concat(time.toString()), StandardCharsets.UTF_8)
                .toString();

        return encodedUrl;
    }

    @Override
    public Url persistShortUrl(Url url) {
        return urlRepository.save(url);
    }

    @Override
    public Url getEncodedUrl(String url) {
        return urlRepository.findByShortUrl(url);
    }

    @Override
    public void deleteShortLink(Url url) {

        urlRepository.delete(url);
    }
}
