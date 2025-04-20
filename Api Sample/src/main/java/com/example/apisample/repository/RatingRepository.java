package com.example.apisample.repository;


import com.example.apisample.entity.Product;
import com.example.apisample.entity.Rating;
import com.example.apisample.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface RatingRepository extends JpaRepository<Rating, Integer> {
    Page<Rating> findAllByDeletedFalse(Pageable pageable);

    Optional<Rating> findTopByUserAndProduct(User user, Product product);
}
