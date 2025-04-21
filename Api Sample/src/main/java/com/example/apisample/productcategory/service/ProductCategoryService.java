package com.example.apisample.productcategory.service;

import com.example.apisample.category.exception.CategoryNotFoundException;
import com.example.apisample.product.exception.ProductNotFoundException;
import com.example.apisample.productcategory.exception.ProductCategoryNotFoundException;
import com.example.apisample.productcategory.model.dto.ProductCategoryRequestDTO;
import com.example.apisample.productcategory.model.dto.ProductCategoryResponseDTO;
import com.example.apisample.utils.pagination.APIPageableResponseDTO;

public interface ProductCategoryService {
    void addCategoryToProduct(ProductCategoryRequestDTO requestDTO);
    void removeCategoryFromProduct(Integer productId, Integer categoryId);
    APIPageableResponseDTO<ProductCategoryResponseDTO> getAllProductCategories(int pageNo, int pageSize, String search, String sort);
    ProductCategoryResponseDTO getProductCategoriesByProductId(Integer productId);
}
