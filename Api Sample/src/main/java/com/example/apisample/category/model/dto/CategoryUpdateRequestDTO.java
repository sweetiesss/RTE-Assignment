package com.example.apisample.category.model.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryUpdateRequestDTO {

    @Size(max = 50, message = "Category name must not exceed 50 characters")
    private String name;

    private String description;
}
