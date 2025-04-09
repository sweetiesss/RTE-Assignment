package com.example.apisample.service.Implement;

import com.example.apisample.entity.Product;
import com.example.apisample.exception.productservice.ProductDeletedException;
import com.example.apisample.exception.productservice.ProductNotFoundException;
import com.example.apisample.model.dto.pagination.APIPageableResponseDTO;
import com.example.apisample.model.dto.product.ProductCreateDTO;
import com.example.apisample.model.dto.product.ProductResponseDTO;
import com.example.apisample.model.dto.product.ProductUpdateDTO;
import com.example.apisample.model.mapper.ProductMapper;
import com.example.apisample.repository.ProductRepository;
import com.example.apisample.service.Interface.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;


@Service
@RequiredArgsConstructor
public class ProductServiceImp implements ProductService {
    private final ProductRepository productRepository;

    public APIPageableResponseDTO<ProductResponseDTO> getALlProduct(int pageNo, int pageSize, String search, String sortField) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        Page<Product> page = productRepository.findByDeletedFalse(pageable);
        Page<ProductResponseDTO> productDtoPage = page.map(ProductMapper::productToDTO);

        return new APIPageableResponseDTO<>(productDtoPage);
    }


    public ProductResponseDTO getProductById(Integer id) throws ProductNotFoundException, ProductDeletedException {
        Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);

        if (product.getDeleted()) {
            throw new ProductDeletedException();
        }

        return ProductMapper.productToDTO(product);
    }

    public void createProduct(ProductCreateDTO dto) {
        Product product = new Product();

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setFeatured(dto.getFeatured());
        product.setDeleted(Boolean.FALSE);
        product.setCreateOn(Instant.now());
        product.setLastUpdateOn(Instant.now());

        productRepository.save(product);
    }


    public void updateProduct(Integer id, ProductUpdateDTO dto) throws ProductNotFoundException, ProductDeletedException {
        Product product = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);

        if (!product.getDeleted()) {
            throw new ProductDeletedException();
        }

        if (dto.getName() != null) product.setName(dto.getName());
        if (dto.getDescription() != null) product.setDescription(dto.getDescription());
        if (dto.getPrice() != null) product.setPrice(dto.getPrice());
        if (dto.getFeatured() != null) product.setFeatured(dto.getFeatured());

        product.setLastUpdateOn(Instant.now());

        productRepository.save(product);
    }

    public void deleteProduct(Integer id) throws ProductNotFoundException {
        Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        product.setDeleted(Boolean.TRUE);
        product.setLastUpdateOn(Instant.now());
        productRepository.save(product);
    }

    public void restoreProduct(Integer id) throws ProductNotFoundException, ProductDeletedException {
        Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);

        if (!product.getDeleted()) {
            throw new ProductDeletedException();
        }

        product.setDeleted(Boolean.FALSE);
        product.setLastUpdateOn(Instant.now());
        productRepository.save(product);
    }
}
