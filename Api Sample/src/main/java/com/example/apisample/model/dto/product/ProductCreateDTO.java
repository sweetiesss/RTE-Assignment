package com.example.apisample.model.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductCreateDTO {
    private String name;
    private String description;
    private Long price;
    private Boolean featured = false;
}
