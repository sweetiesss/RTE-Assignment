package com.example.apisample.productcategory.repository;

import com.example.apisample.productcategory.entity.ProductCategory;
import com.example.apisample.productcategory.entity.ProductCategoryId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, ProductCategoryId> {
    List<ProductCategory> findAllByProduct_Id(Integer productId);
    Page<ProductCategory> findAllByCategory_Id(Integer category_id, Pageable pageable);
}
