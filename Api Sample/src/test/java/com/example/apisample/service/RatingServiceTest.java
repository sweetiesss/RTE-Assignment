package com.example.apisample.service;

import com.example.apisample.product.entity.Product;
import com.example.apisample.product.repository.ProductRepository;
import com.example.apisample.rating.entity.Rating;
import com.example.apisample.rating.exception.RatingDeletedException;
import com.example.apisample.rating.exception.RatingHasBeenMadeException;
import com.example.apisample.rating.exception.RatingHasBeenRestoreException;
import com.example.apisample.rating.model.dto.RatingRequestDTO;
import com.example.apisample.rating.model.dto.RatingResponseDTO;
import com.example.apisample.rating.model.dto.RatingUpdateRequestDTO;
import com.example.apisample.rating.repository.RatingRepository;
import com.example.apisample.rating.service.impl.RatingServiceImpl;
import com.example.apisample.user.entity.User;
import com.example.apisample.user.repository.UserRepository;
import com.example.apisample.utils.pagination.APIPageableResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RatingServiceTest {

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private RatingServiceImpl ratingService;

    @Test
    void createRating_ShouldCreateRating_WhenNotExist() {
        // Arrange
        RatingRequestDTO requestDTO = new RatingRequestDTO(1, 1, 4, "Good product");
        User user = new User();
        user.setId(1);
        Product product = new Product();
        product.setId(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(ratingRepository.findTopByUserAndProductAndDeletedFalse(user, product)).thenReturn(Optional.empty()); // No existing rating

        // Act
        ratingService.createRating(requestDTO);

        // Assert
        verify(ratingRepository, times(1)).save(any(Rating.class)); // Verify that the save method is called
    }

    @Test
    void createRating_ShouldThrowRatingHasBeenMadeException_WhenRatingAlreadyExists() {
        // Arrange
        RatingRequestDTO requestDTO = new RatingRequestDTO(1, 1, 4, "Good product");
        User user = new User();
        user.setId(1);
        Product product = new Product();
        product.setId(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(ratingRepository.findTopByUserAndProductAndDeletedFalse(user, product)).thenReturn(Optional.of(new Rating())); // Rating already exists

        // Act & Assert
        assertThrows(RatingHasBeenMadeException.class, () -> ratingService.createRating(requestDTO));
    }

    @Test
    void getRatingsByProductId_ShouldReturnPagedRatings() {
        // Arrange
        Integer productId = 1;
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("createOn"))); // Match sorting by 'createOn' DESC
        List<Rating> ratings = Arrays.asList(
                new Rating(1, new User(), new Product(), 4, "Good", Instant.now(), Instant.now(), false, 1L),
                new Rating(2, new User(), new Product(), 5, "Excellent", Instant.now(), Instant.now(), false, 1L)
        );
        Page<Rating> ratingPage = new PageImpl<>(ratings, pageable, ratings.size());

        // Mock repository call with matching Pageable argument (including sorting)
        when(ratingRepository.findAllByProduct_IdAndDeletedFalse(eq(productId), eq(pageable))).thenReturn(ratingPage);

        // Act
        APIPageableResponseDTO<RatingResponseDTO> response = ratingService.getRatingsByProductId(productId, 0, 10, "createOn");

        // Assert
        assertNotNull(response);
        assertEquals(2, response.getContent().size()); // Assert the size of the returned list
    }


    @Test
    void calculateAverageRating_ShouldReturnCorrectAverage() {
        // Arrange
        Integer productId = 1;
        List<Rating> ratings = Arrays.asList(
                new Rating(1, new User(), new Product(), 5, "Excellent", Instant.now(), Instant.now(), false, 1L),
                new Rating(2, new User(), new Product(), 4, "Good", Instant.now(), Instant.now(), false, 1L)
        );

        when(ratingRepository.findAllByProduct_IdAndDeletedFalse(productId)).thenReturn(ratings);

        // Act
        Double average = ratingService.calculateAverageRating(productId);

        // Assert
        assertEquals(4.5, average); // Assert the correct average rating
    }

    @Test
    void getRatingById_ShouldReturnRating() {
        // Arrange
        Integer ratingId = 1;
        Rating rating = new Rating(1, new User(), new Product(), 4, "Good", Instant.now(), Instant.now(), false, 1L);
        when(ratingRepository.findById(ratingId)).thenReturn(Optional.of(rating));

        // Act
        RatingResponseDTO response = ratingService.getRatingById(ratingId);

        // Assert
        assertNotNull(response);
        assertEquals("Good", response.getDescription()); // Assert description matches
    }

    @Test
    void getRatingById_ShouldThrowRatingDeletedException_WhenDeleted() {
        // Arrange
        Integer ratingId = 1;
        Rating rating = new Rating(1, new User(), new Product(), 4, "Good", Instant.now(), Instant.now(), true, 1L);
        when(ratingRepository.findById(ratingId)).thenReturn(Optional.of(rating));

        // Act & Assert
        assertThrows(RatingDeletedException.class, () -> ratingService.getRatingById(ratingId));
    }

    @Test
    void updateRating_ShouldUpdateRating_WhenExists() {
        // Arrange
        Integer ratingId = 1;
        RatingUpdateRequestDTO updateRequest = new RatingUpdateRequestDTO(5, "Excellent product");
        Rating rating = new Rating(1, new User(), new Product(), 4, "Good", Instant.now(), Instant.now(), false, 1L);
        when(ratingRepository.findById(ratingId)).thenReturn(Optional.of(rating));

        // Act
        ratingService.updateRating(ratingId, updateRequest);

        // Assert
        assertEquals(5, rating.getPoint()); // Assert the rating point is updated
        assertEquals("Excellent product", rating.getDescription()); // Assert the description is updated
        verify(ratingRepository, times(1)).save(rating); // Verify that the save method is called
    }

    @Test
    void deleteRating_ShouldDeleteRating() {
        // Arrange
        Integer ratingId = 1;
        Rating rating = new Rating(1, new User(), new Product(), 4, "Good", Instant.now(), Instant.now(), false, 1L);
        when(ratingRepository.findById(ratingId)).thenReturn(Optional.of(rating));

        // Act
        ratingService.deleteRating(ratingId);

        // Assert
        assertTrue(rating.getDeleted()); // Assert that the rating is marked as deleted
        verify(ratingRepository, times(1)).save(rating); // Verify that the save method is called to persist the deletion
    }

    @Test
    void restoreRating_ShouldRestoreRating_WhenDeleted() {
        // Arrange
        Integer ratingId = 1;
        Rating rating = new Rating(1, new User(), new Product(), 4, "Good", Instant.now(), Instant.now(), true, 1L);
        when(ratingRepository.findById(ratingId)).thenReturn(Optional.of(rating));

        // Act
        ratingService.restoreRating(ratingId);

        // Assert
        assertFalse(rating.getDeleted()); // Assert that the rating is restored
        verify(ratingRepository, times(1)).save(rating); // Verify that the save method is called to persist the restoration
    }

    @Test
    void restoreRating_ShouldThrowRatingHasBeenRestoreException_WhenNotDeleted() {
        // Arrange
        Integer ratingId = 1;
        Rating rating = new Rating(1, new User(), new Product(), 4, "Good", Instant.now(), Instant.now(), false, 1L);
        when(ratingRepository.findById(ratingId)).thenReturn(Optional.of(rating));

        // Act & Assert
        assertThrows(RatingHasBeenRestoreException.class, () -> ratingService.restoreRating(ratingId));
    }
}


