package com.example.apisample.rating.service.impl;

import com.example.apisample.product.entity.Product;
import com.example.apisample.product.repository.ProductRepository;
import com.example.apisample.rating.entity.Rating;
import com.example.apisample.rating.exception.RatingDeletedException;
import com.example.apisample.rating.exception.RatingHasBeenMadeException;
import com.example.apisample.rating.exception.RatingHasBeenRestoreException;
import com.example.apisample.rating.exception.RatingNotFoundException;
import com.example.apisample.rating.model.dto.RatingRequestDTO;
import com.example.apisample.rating.model.dto.RatingResponseDTO;
import com.example.apisample.rating.model.dto.RatingUpdateRequestDTO;
import com.example.apisample.rating.model.mapper.RatingMapper;
import com.example.apisample.rating.repository.RatingRepository;
import com.example.apisample.rating.service.RatingService;
import com.example.apisample.user.entity.User;
import com.example.apisample.user.exception.UserDoesNotExistException;
import com.example.apisample.user.repository.UserRepository;
import com.example.apisample.utils.pagination.APIPageableResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional(readOnly = true)
    public APIPageableResponseDTO<RatingResponseDTO> getRatingsByProductId(Integer productId, int pageNo, int pageSize, String sort) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sort).descending());
        Page<Rating> ratingPage = ratingRepository.findAllByProduct_IdAndDeletedFalse(productId, pageable);

        List<RatingResponseDTO> dtoList = ratingPage.getContent().stream()
                .map(RatingMapper::ratingToDTO)
                .toList();

        Page<RatingResponseDTO> dtoPage = new PageImpl<>(dtoList, pageable, ratingPage.getTotalElements());
        return new APIPageableResponseDTO<>(dtoPage);
    }

    @Override
    public Double calculateAverageRating(Integer productId) {
        List<Rating> ratings = ratingRepository.findAllByProduct_IdAndDeletedFalse(productId);

        if (ratings.isEmpty()) return 0.0;

        double sum = 0.0;
        for (Rating rating : ratings) {
            sum += rating.getPoint();
        }

        return sum / ratings.size();
    }


    @Override
    @Transactional
    public void createRating(RatingRequestDTO request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(UserDoesNotExistException::new);
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(RatingNotFoundException::new);

        Optional<Rating> optionalRating = ratingRepository.findTopByUserAndProductAndDeletedFalse(user, product);

        if(optionalRating.isPresent()) {
            throw new RatingHasBeenMadeException();
        }

        Rating rating = Rating.builder()
                .user(user)
                .product(product)
                .point(request.getPoint())
                .description(request.getDescription())
                .deleted(Boolean.FALSE)
                .build();

        ratingRepository.save(rating);
    }

    @Override
    public RatingResponseDTO getRatingById(Integer id) {
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(RatingNotFoundException::new);

        if (rating.getDeleted()) {
            throw new RatingDeletedException();
        }

        return RatingMapper.ratingToDTO(rating);
    }

    @Transactional
    public APIPageableResponseDTO<RatingResponseDTO> getAllRating(int pageNo, int pageSize, String search, String sortField) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortField).ascending());

        Page<Rating> page = ratingRepository.findAllByDeletedFalse(pageable);
        Page<RatingResponseDTO> ratingDtoPage = page.map(RatingMapper::ratingToDTO);

        return new APIPageableResponseDTO<>(ratingDtoPage);
    }

    @Override
    @Transactional
    public void updateRating(Integer id, RatingUpdateRequestDTO request) {
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(RatingNotFoundException::new);

        if (rating.getDeleted()) {
            throw new RatingDeletedException();
        }

        if (request.getPoint() != null) rating.setPoint(request.getPoint());
        if (request.getDescription() != null) rating.setDescription(request.getDescription());

        ratingRepository.save(rating);
    }

    @Override
    @Transactional
    public void deleteRating(Integer id) {
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(RatingNotFoundException::new);

        if (rating.getDeleted()) {
            throw new RatingDeletedException();
        }

        rating.setDeleted(Boolean.TRUE);
        ratingRepository.save(rating);
    }

    @Override
    @Transactional
    public void restoreRating(Integer id) throws RatingNotFoundException, RatingHasBeenRestoreException {
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(RatingNotFoundException::new);

        if (!rating.getDeleted()) {
            throw new RatingHasBeenRestoreException();
        }

        rating.setDeleted(Boolean.FALSE);
        ratingRepository.save(rating);
    }
}
