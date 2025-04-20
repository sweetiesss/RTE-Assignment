package com.example.apisample.service.Implement;

import com.example.apisample.entity.Product;
import com.example.apisample.entity.User;
import com.example.apisample.exception.productservice.ProductNotFoundException;
import com.example.apisample.model.dto.pagination.APIPageableResponseDTO;
import com.example.apisample.model.dto.product.ProductResponseDTO;
import com.example.apisample.model.dto.user.UserResponseDTO;
import com.example.apisample.model.mapper.ProductMapper;
import com.example.apisample.model.mapper.UserMapper;
import com.example.apisample.repository.ProductRepository;
import com.example.apisample.service.Interface.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ProductServiceImp implements ProductService {
    private final ProductRepository productRepository;

    public APIPageableResponseDTO<ProductResponseDTO> getALlProduct(int pageNo, int pageSize, String search, String sortField) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        Page<Product> page = productRepository.findByNameContaining(search, pageable);
        Page<ProductResponseDTO> productDtoPage = page.map(ProductMapper::productToDTO);

        return new APIPageableResponseDTO<>(productDtoPage);
    }

    public ProductResponseDTO getProductById(Integer id) throws ProductNotFoundException {
        Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        return ProductMapper.productToDTO(product);
    }
}
