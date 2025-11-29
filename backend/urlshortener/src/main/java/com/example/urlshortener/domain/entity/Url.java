package com.example.urlshortener.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "urls")
@Data
public class Url {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    @Lob
    private String originalUrl;

    private String shortUrl;

    @CreatedDate
    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;
}
