package com.example.apisample.rating.model.mapper;

import com.example.apisample.rating.entity.Rating;
import com.example.apisample.rating.model.dto.RatingResponseDTO;

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
