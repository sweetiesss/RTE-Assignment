package com.example.apisample.repository;

import com.example.apisample.entity.Product;
import com.example.apisample.entity.ProductCategory;
import com.example.apisample.entity.ProductCategoryId;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ProductCategoryRepository extends JpaRepository<ProductCategory, ProductCategoryId> {
    List<ProductCategory> findAllByProduct_Id(Integer productId);
}
