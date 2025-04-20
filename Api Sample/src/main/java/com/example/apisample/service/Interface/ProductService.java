package com.example.apisample.service.Interface;

import com.example.apisample.exception.productservice.ProductNotFoundException;
import com.example.apisample.model.dto.pagination.APIPageableResponseDTO;
import com.example.apisample.model.dto.product.ProductResponseDTO;

public interface ProductService {
    APIPageableResponseDTO<ProductResponseDTO> getALlProduct(int pageNo, int pageSize, String search, String sortField);
    ProductResponseDTO getProductById(Integer id) throws ProductNotFoundException;
}
