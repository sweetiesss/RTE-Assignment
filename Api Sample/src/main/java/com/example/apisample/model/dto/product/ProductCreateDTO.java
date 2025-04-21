package com.example.apisample.model.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductCreateDTO {

    @NotBlank(message = "Product name is required")
    @Size(max = 50, message = "Product name must not exceed 50 characters")
    private String name;

    private String description;

    @NotNull(message = "Product price is required")
    @Positive(message = "Product price must be greater than zero")
    private Long price;

    private Boolean featured = false;
}
