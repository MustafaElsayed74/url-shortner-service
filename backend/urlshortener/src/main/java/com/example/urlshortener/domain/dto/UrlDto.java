package com.example.urlshortener.domain.dto;

import lombok.Data;

@Data
public class UrlDto {

    private String url;

    private String expiresAt; //optional
}
