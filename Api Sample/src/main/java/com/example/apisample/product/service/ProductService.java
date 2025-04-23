package com.example.apisample.product.service;

import com.example.apisample.product.exception.ProductDeletedException;
import com.example.apisample.product.exception.ProductNotFoundException;
import com.example.apisample.product.model.dto.ProductCreateDTO;
import com.example.apisample.product.model.dto.ProductResponseDTO;
import com.example.apisample.product.model.dto.ProductUpdateDTO;
import com.example.apisample.utils.pagination.APIPageableResponseDTO;

public interface ProductService {
    APIPageableResponseDTO<ProductResponseDTO> getALlProduct(int pageNo, int pageSize, String search, String sortField);
    APIPageableResponseDTO<ProductResponseDTO> getALlFeaturedProduct(int pageNo, int pageSize, String search, String sortField);
    ProductResponseDTO getProductById(Integer id);
    void createProduct(ProductCreateDTO dto);
    void updateProduct(Integer id, ProductUpdateDTO dto);
    void deleteProduct(Integer id);
    void restoreProduct(Integer id);
}
