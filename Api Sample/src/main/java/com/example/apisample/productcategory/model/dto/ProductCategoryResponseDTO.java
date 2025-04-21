package com.example.apisample.productcategory.model.dto;

import com.example.apisample.category.model.dto.CategoryResponseDTO;
import lombok.Data;

import java.util.List;

@Data
public class ProductCategoryResponseDTO {
    private Integer productId;
    private String productName;
    private List<CategoryResponseDTO> categories;
}
