package com.example.apisample.model.mapper;

import com.example.apisample.entity.Category;
import com.example.apisample.model.dto.category.CategoryRequestDTO;
import com.example.apisample.model.dto.category.CategoryResponseDTO;

public class CategoryMapper {

    public static CategoryResponseDTO categoryToDTO(Category category) {
        return CategoryResponseDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }
}
