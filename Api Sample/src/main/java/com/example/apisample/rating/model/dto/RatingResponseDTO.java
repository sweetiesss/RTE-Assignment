package com.example.apisample.rating.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class RatingResponseDTO {
    private Integer id;
    private String userEmail;
    private String productName;
    private Integer point;
    private String description;
    private String deleted;
    private Instant createOn;
    private Instant lastUpdateOn;   
}
