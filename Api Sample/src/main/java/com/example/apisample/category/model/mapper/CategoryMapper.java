package com.example.apisample.category.model.mapper;

import com.example.apisample.category.entity.Category;
import com.example.apisample.category.model.dto.CategoryResponseDTO;

public class CategoryMapper {

    public static CategoryResponseDTO categoryToDTO(Category category) {
        return CategoryResponseDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }
}
