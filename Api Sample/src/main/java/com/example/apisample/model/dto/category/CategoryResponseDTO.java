package com.example.apisample.model.dto.category;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryResponseDTO {
    private Integer id;
    private String name;
    private String description;
}
