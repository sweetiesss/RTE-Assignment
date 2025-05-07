package com.example.apisample.rating.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
