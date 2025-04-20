package com.example.apisample.model.dto.rating;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RatingRequestDTO {
    private Integer userId;
    private Integer productId;
    private Integer point;
    private String description;
}
