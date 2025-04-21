package com.example.apisample.model.mapper;

import com.example.apisample.entity.Category;
import com.example.apisample.entity.ProductCategory;
import com.example.apisample.model.dto.category.CategoryResponseDTO;
import com.example.apisample.model.dto.productcategory.ProductCategoryResponseDTO;

import java.util.ArrayList;
import java.util.List;

public class ProductCategoryMapper {

    public static ProductCategoryResponseDTO productCategoryToDTO(ProductCategory productCategory) {
        ProductCategoryResponseDTO responseDTO = new ProductCategoryResponseDTO();

        // Initialize the categories list
        List<CategoryResponseDTO> categories = new ArrayList<>();

        // Populate the product details
        responseDTO.setProductId(productCategory.getProduct().getId());
        responseDTO.setProductName(productCategory.getProduct().getName());

        // Add category information to the list
        categories.add(CategoryResponseDTO.builder()
                .id(productCategory.getCategory().getId())
                .name(productCategory.getCategory().getName())
                .description(productCategory.getCategory().getDescription())
                .build());

        // Set the categories in the responseDTO
        responseDTO.setCategories(categories);

        return responseDTO;
    }
}

