package com.example.apisample.model.mapper;

import com.example.apisample.entity.Product;
import com.example.apisample.model.dto.product.ProductResponseDTO;

public class ProductMapper {
    private static final String DELETED_STATUS = "Deleted";
    private static final String NOT_DELETED_STATUS = "Not Deleted";

    public static ProductResponseDTO productToDTO(Product product) {
        return ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .image(product.getImage())
                .featured(product.getFeatured())
                .deleted(product.getDeleted() ? DELETED_STATUS : NOT_DELETED_STATUS)
                .createOn(product.getCreateOn())
                .lastUpdateOn(product.getLastUpdateOn())
                .build();
    }
}
