package com.example.urlshortener.domain.dto;


import lombok.Data;

@Data
public class UrlErrorResponseDto {
    private int status;
    private String error;
}
