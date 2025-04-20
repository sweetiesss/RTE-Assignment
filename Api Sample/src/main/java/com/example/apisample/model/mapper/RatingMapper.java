package com.example.apisample.model.mapper;

import com.example.apisample.entity.Rating;
import com.example.apisample.model.dto.rating.RatingResponseDTO;

public class RatingMapper {
    private static final String DELETED = "Deleted";
    private static final String NOT_DELETED = "Not Deleted";

    public static RatingResponseDTO ratingToDTO(Rating rating) {
        return RatingResponseDTO.builder()
                .id(rating.getId())
                .userEmail(rating.getUser().getEmail())
                .productName(rating.getProduct().getName())
                .point(rating.getPoint())
                .description(rating.getDescription())
                .deleted(rating.getDeleted() ? DELETED : NOT_DELETED)
                .createOn(rating.getCreateOn())
                .lastUpdateOn(rating.getLastUpdateOn())
                .build();
    }
}
