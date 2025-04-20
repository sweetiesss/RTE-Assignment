package com.example.apisample.service.Interface;

import com.example.apisample.exception.productservice.ProductDeletedException;
import com.example.apisample.exception.productservice.ProductNotFoundException;
import com.example.apisample.model.dto.pagination.APIPageableResponseDTO;
import com.example.apisample.model.dto.product.ProductCreateDTO;
import com.example.apisample.model.dto.product.ProductResponseDTO;
import com.example.apisample.model.dto.product.ProductUpdateDTO;

public interface ProductService {
    APIPageableResponseDTO<ProductResponseDTO> getALlProduct(int pageNo, int pageSize, String search, String sortField);
    ProductResponseDTO getProductById(Integer id) throws ProductNotFoundException, ProductDeletedException;
    void createProduct(ProductCreateDTO dto);
    void updateProduct(Integer id, ProductUpdateDTO dto) throws ProductNotFoundException;
    void deleteProduct(Integer id) throws ProductNotFoundException;
    void restoreProduct(Integer id) throws ProductNotFoundException;
}
