package com.example.apisample.product.model.mapper;

import com.example.apisample.product.entity.Product;
import com.example.apisample.product.model.dto.ProductResponseDTO;

public class ProductMapper {
    private static final String DELETED_STATUS = "Deleted";
    private static final String NOT_DELETED_STATUS = "Not Deleted";

    public static ProductResponseDTO productToDTO(Product product, Double averageRating) {
        return ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .image(product.getImage())
                .featured(product.getFeatured())
                .deleted(product.getDeleted() ? "Deleted" : "Not Deleted")
                .createOn(product.getCreateOn())
                .lastUpdateOn(product.getLastUpdateOn())
                .averageRating(averageRating)
                .build();
    }

}
