package com.example.apisample.model.dto.product;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
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
}
