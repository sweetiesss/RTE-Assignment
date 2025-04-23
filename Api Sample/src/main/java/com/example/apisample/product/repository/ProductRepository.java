package com.example.apisample.product.repository;

import com.example.apisample.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Page<Product> findByDeletedFalse(Pageable pageable);
    Page<Product> findByDeletedFalseAndFeaturedTrue(Pageable pageable);
}
