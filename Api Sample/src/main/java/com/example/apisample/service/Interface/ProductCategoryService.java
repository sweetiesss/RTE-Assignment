package com.example.apisample.service.Interface;

import com.example.apisample.exception.categoryservice.CategoryNotFoundException;
import com.example.apisample.exception.productcategoryservice.ProductCategoryNotFoundException;
import com.example.apisample.exception.productservice.ProductNotFoundException;
import com.example.apisample.model.dto.pagination.APIPageableResponseDTO;
import com.example.apisample.model.dto.productcategory.ProductCategoryRequestDTO;
import com.example.apisample.model.dto.productcategory.ProductCategoryResponseDTO;

import java.util.List;

public interface ProductCategoryService {
    void addCategoryToProduct(ProductCategoryRequestDTO requestDTO) throws ProductNotFoundException, CategoryNotFoundException;
    void removeCategoryFromProduct(Integer productId, Integer categoryId) throws ProductCategoryNotFoundException;
    APIPageableResponseDTO<ProductCategoryResponseDTO> getAllProductCategories(int pageNo, int pageSize, String search, String sort);
    ProductCategoryResponseDTO getProductCategoriesByProductId(Integer productId) throws ProductCategoryNotFoundException;
}
