package com.example.urlshortener.controller;

import com.example.urlshortener.domain.dto.UrlDto;
import com.example.urlshortener.domain.dto.UrlErrorResponseDto;
import com.example.urlshortener.domain.dto.UrlResponseDto;
import com.example.urlshortener.domain.entity.Url;
import com.example.urlshortener.service.UrlService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/urls")
public class UrlController {
    private final UrlService urlService;

    @PostMapping("/generate")
    public ResponseEntity<?> generateShortLink(@RequestBody UrlDto urlDto) {

        Url urlToReturn = urlService.generateShortLink(urlDto);
        if (urlToReturn != null) {
            UrlResponseDto urlResponseDto = new UrlResponseDto();

            urlResponseDto.setOriginalUrl(urlDto.getUrl());
            urlResponseDto.setShortLink(urlToReturn.getShortUrl());
            urlResponseDto.setExpiresAt(urlToReturn.getExpiresAt());

            return ResponseEntity.ok(urlResponseDto);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();


    }


    @GetMapping("{shortLink}")
    public ResponseEntity<?> redirectToOriginalUrl(@PathVariable String shortLink,HttpServletResponse response) throws IOException {
        if (StringUtils.isEmpty(shortLink)) {
            UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();

            urlErrorResponseDto.setStatus(400);
            urlErrorResponseDto.setError("Invalid Url");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(urlErrorResponseDto);
        }

        Url urlToReturn = urlService.getEncodedUrl(shortLink);
        if (urlToReturn == null) {
            UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();

            urlErrorResponseDto.setStatus(400);
            urlErrorResponseDto.setError("Url does not exist, or it might have expired!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(urlErrorResponseDto);

        }

        if(urlToReturn.getExpiresAt().isBefore(LocalDateTime.now())){
            urlService.deleteShortLink(urlToReturn);
            UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();

            urlErrorResponseDto.setStatus(200);
            urlErrorResponseDto.setError("Url expired!");
            return ResponseEntity.status(HttpStatus.OK)
                    .body(urlErrorResponseDto);

        }

        response.sendRedirect(urlToReturn.getOriginalUrl());
        return null;
    }
}
