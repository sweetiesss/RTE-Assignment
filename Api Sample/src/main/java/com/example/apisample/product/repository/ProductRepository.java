package com.example.apisample.product.repository;

import com.example.apisample.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Page<Product> findByNameContainingAndDeletedFalse(Pageable pageable, String search);
    List<Product> findByNameContainingIgnoreCaseAndDeletedFalse(String search);
    Page<Product> findByDeletedFalseAndFeaturedTrue(Pageable pageable);
}
