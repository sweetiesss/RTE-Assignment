package com.example.apisample.productcategory.model.dto;

import com.example.apisample.category.model.dto.CategoryResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryResponseDTO {
    private Integer productId;
    private String productName;
    private List<CategoryResponseDTO> categories;
}
