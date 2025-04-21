package com.example.apisample.product.model.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductUpdateDTO {

    @Size(max = 50, message = "Product name must not exceed 50 characters")
    private String name;
    private String description;
    @Positive(message = "Product price must be greater than zero")
    private Long price;
    private Boolean featured;
    private Boolean deleted;
}
