package com.example.apisample.service.Interface;

import com.example.apisample.exception.ratingservice.RatingHasBeenMadeException;
import com.example.apisample.exception.ratingservice.RatingNotFoundException;
import com.example.apisample.exception.userservice.UserDoesNotExistException;
import com.example.apisample.model.dto.pagination.APIPageableResponseDTO;
import com.example.apisample.model.dto.rating.RatingRequestDTO;
import com.example.apisample.model.dto.rating.RatingResponseDTO;

import java.util.List;

public interface RatingService {
    RatingResponseDTO createRating(RatingRequestDTO request) throws UserDoesNotExistException, RatingNotFoundException, RatingHasBeenMadeException;
    RatingResponseDTO getRatingById(Integer id) throws Exception;
    APIPageableResponseDTO<RatingResponseDTO> getAllRating(int pageNo, int pageSize, String search, String sortField);
    void updateRating(Integer id, RatingRequestDTO request) throws Exception;
    void deleteRating(Integer id) throws Exception;
    void restoreRating(Integer id) throws Exception;
}
