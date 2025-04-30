package com.example.apisample.rating.service;

import com.example.apisample.rating.model.dto.RatingRequestDTO;
import com.example.apisample.rating.model.dto.RatingResponseDTO;
import com.example.apisample.rating.model.dto.RatingUpdateRequestDTO;
import com.example.apisample.utils.pagination.APIPageableResponseDTO;

public interface RatingService {
    APIPageableResponseDTO<RatingResponseDTO> getRatingsByProductId(Integer productId, int pageNo, int pageSize, String sort);
    void createRating(RatingRequestDTO request);
    RatingResponseDTO getRatingById(Integer id);
    APIPageableResponseDTO<RatingResponseDTO> getAllRating(int pageNo, int pageSize, String search, String sortField);
    void updateRating(Integer id, RatingUpdateRequestDTO request);
    void deleteRating(Integer id);
    void restoreRating(Integer id);
    Double calculateAverageRating(Integer productId);
}
