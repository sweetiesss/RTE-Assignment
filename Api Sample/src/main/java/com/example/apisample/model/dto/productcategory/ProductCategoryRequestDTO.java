package com.example.apisample.model.dto.productcategory;

import lombok.Data;

import java.util.List;

@Data
public class ProductCategoryRequestDTO {
    private Integer productId;
    private List<Integer> categoryId;
}
