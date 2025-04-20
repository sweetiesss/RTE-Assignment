package com.example.apisample.repository;

import com.example.apisample.entity.OtpCode;
import com.example.apisample.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Page<Product> findByNameContaining(String name, Pageable pageable);
}
