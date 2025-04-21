package com.example.apisample.model.dto.category;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryUpdateRequestDTO {

    @Size(max = 50, message = "Category name must not exceed 50 characters")
    private String name;

    private String description;
}
