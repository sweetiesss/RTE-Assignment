package com.example.apisample.service.Implementation;

import com.example.apisample.entity.Rating;
import com.example.apisample.entity.Product;
import com.example.apisample.entity.User;
import com.example.apisample.exception.ratingservice.RatingDeletedException;
import com.example.apisample.exception.ratingservice.RatingHasBeenMadeException;
import com.example.apisample.exception.ratingservice.RatingHasBeenRestoreException;
import com.example.apisample.exception.ratingservice.RatingNotFoundException;
import com.example.apisample.exception.userservice.UserDoesNotExistException;
import com.example.apisample.model.dto.pagination.APIPageableResponseDTO;
import com.example.apisample.model.dto.rating.RatingRequestDTO;
import com.example.apisample.model.dto.rating.RatingResponseDTO;
import com.example.apisample.model.mapper.RatingMapper;
import com.example.apisample.repository.RatingRepository;
import com.example.apisample.repository.ProductRepository;
import com.example.apisample.repository.UserRepository;
import com.example.apisample.service.Interface.RatingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    public RatingResponseDTO createRating(RatingRequestDTO request) throws UserDoesNotExistException, RatingNotFoundException, RatingHasBeenMadeException {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(UserDoesNotExistException::new);
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(RatingNotFoundException::new);

        Optional<Rating> optionalRating = ratingRepository.findTopByUserAndProduct(user, product);

        if(optionalRating.isPresent()) {
            throw new RatingHasBeenMadeException();
        }

        Rating rating = Rating.builder()
                .user(user)
                .product(product)
                .point(request.getPoint())
                .description(request.getDescription())
                .createOn(Instant.now())
                .lastUpdateOn(Instant.now())
                .deleted(Boolean.FALSE)
                .build();

        ratingRepository.save(rating);
        return RatingMapper.ratingToDTO(rating);
    }

    @Override
    public RatingResponseDTO getRatingById(Integer id) throws Exception {
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(RatingNotFoundException::new);

        if (rating.getDeleted()) {
            throw new RatingDeletedException();
        }

        return RatingMapper.ratingToDTO(rating);
    }

    @Transactional
    public APIPageableResponseDTO<RatingResponseDTO> getAllRating(int pageNo, int pageSize, String search, String sortField) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        Page<Rating> page = ratingRepository.findAllByDeletedFalse(pageable);
        Page<RatingResponseDTO> ratingDtoPage = page.map(RatingMapper::ratingToDTO);

        return new APIPageableResponseDTO<>(ratingDtoPage);
    }

    @Override
    public void updateRating(Integer id, RatingRequestDTO request) throws Exception {
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(RatingNotFoundException::new);

        if (rating.getDeleted()) {
            throw new RatingDeletedException();
        }

        if (request.getPoint() != null) rating.setPoint(request.getPoint());
        if (request.getDescription() != null) rating.setDescription(request.getDescription());
        rating.setLastUpdateOn(Instant.now());

        ratingRepository.save(rating);
    }

    @Override
    public void deleteRating(Integer id) throws Exception {
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(RatingNotFoundException::new);

        if (rating.getDeleted()) {
            throw new RatingDeletedException();
        }

        rating.setDeleted(Boolean.TRUE);
        ratingRepository.save(rating);
    }

    @Override
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
