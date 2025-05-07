package com.example.apisample.productcategory.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductCategoryRequestDTO {

    @NotNull(message = "Product ID is required")
    @Min(value = 1, message = "Product ID must be greater than 0")
    private Integer productId;

    @NotEmpty(message = "At least one category ID is required")
    private List<@NotNull(message = "Category ID cannot be null") @Min(value = 1, message = "Category ID must be greater than 0") Integer> categoryId;
}
