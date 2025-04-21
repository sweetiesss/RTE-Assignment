package com.example.apisample.model.dto.productcategory;

import com.example.apisample.model.dto.category.CategoryResponseDTO;
import lombok.Data;

import java.util.List;

@Data
public class ProductCategoryResponseDTO {
    private Integer productId;
    private String productName;
    private List<CategoryResponseDTO> categories;
}
