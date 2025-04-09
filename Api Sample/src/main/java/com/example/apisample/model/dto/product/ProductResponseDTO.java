package com.example.apisample.model.dto.product;

import lombok.*;

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
}
