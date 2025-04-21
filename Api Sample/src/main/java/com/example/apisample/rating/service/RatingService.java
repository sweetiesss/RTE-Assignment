package com.example.apisample.rating.service;

import com.example.apisample.rating.exception.RatingHasBeenMadeException;
import com.example.apisample.rating.exception.RatingNotFoundException;
import com.example.apisample.rating.model.dto.RatingRequestDTO;
import com.example.apisample.rating.model.dto.RatingResponseDTO;
import com.example.apisample.user.exception.UserDoesNotExistException;
import com.example.apisample.utils.pagination.APIPageableResponseDTO;

public interface RatingService {
    RatingResponseDTO createRating(RatingRequestDTO request);
    RatingResponseDTO getRatingById(Integer id);
    APIPageableResponseDTO<RatingResponseDTO> getAllRating(int pageNo, int pageSize, String search, String sortField);
    void updateRating(Integer id, RatingRequestDTO request);
    void deleteRating(Integer id);
    void restoreRating(Integer id);
}
