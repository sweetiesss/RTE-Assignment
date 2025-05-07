package com.example.apisample.product.service;

import com.example.apisample.product.model.dto.ProductCreateDTO;
import com.example.apisample.product.model.dto.ProductResponseDTO;
import com.example.apisample.product.model.dto.ProductUpdateDTO;
import com.example.apisample.utils.pagination.APIPageableResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.Serializable;

public interface ProductService {
    APIPageableResponseDTO<ProductResponseDTO> getAllProduct(
            int pageNo,
            int pageSize,
            String search,
            String sortField
    );
    APIPageableResponseDTO<ProductResponseDTO> getALlFeaturedProduct(int pageNo, int pageSize, String search, String sort);
    ProductResponseDTO getProductById(Integer id);
    void createProduct(ProductCreateDTO dto);
    void updateProduct(Integer id, ProductUpdateDTO dto);
    void deleteProduct(Integer id);
    void restoreProduct(Integer id);
    String uploadProductImage(Integer productId, MultipartFile file) throws IOException;
}
