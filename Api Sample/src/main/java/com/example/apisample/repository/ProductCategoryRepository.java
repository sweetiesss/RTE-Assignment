package com.example.apisample.repository;

import com.example.apisample.entity.ProductCategory;
import com.example.apisample.entity.ProductCategoryId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, ProductCategoryId> {
    List<ProductCategory> findByProduct_Id(Integer productId);

    List<ProductCategory> findByCategory_Id(Integer categoryId);
}
