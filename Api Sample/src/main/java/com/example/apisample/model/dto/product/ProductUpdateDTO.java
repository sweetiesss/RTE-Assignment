package com.example.apisample.model.dto.product;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductUpdateDTO {
    private String name;
    private String description;
    private Long price;
    private Boolean featured;
}