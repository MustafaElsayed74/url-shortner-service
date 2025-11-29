package com.example.urlshortener.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UrlDto {

    private String url;

    private String expiresAt; //optional
}
