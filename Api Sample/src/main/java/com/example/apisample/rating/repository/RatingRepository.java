package com.example.apisample.rating.repository;


import com.example.apisample.product.entity.Product;
import com.example.apisample.rating.entity.Rating;
import com.example.apisample.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

// findAllByProduct_IdAndDeletedFalse
public interface RatingRepository extends JpaRepository<Rating, Integer> {
    Page<Rating> findAllByDeletedFalse(Pageable pageable);
    Page<Rating> findAllByProduct_IdAndDeletedFalse(Integer productId, Pageable pageable);
    List<Rating> findAllByProduct_IdAndDeletedFalse(Integer productId);
    Optional<Rating> findTopByUserAndProduct(User user, Product product);
}
