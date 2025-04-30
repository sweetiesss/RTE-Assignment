package com.example.apisample.product.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ProductResponseDTO {
    private Integer id;
    private String name;
    private String description;
    private Long price;
    private String image;
    private Boolean featured;
    private String deleted;
    private Instant createOn;
    private Instant lastUpdateOn;
    private Double averageRating;
}
