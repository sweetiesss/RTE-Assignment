package com.example.apisample.product.service.impl;

import com.example.apisample.product.entity.Product;
import com.example.apisample.product.exception.ProductDeletedException;
import com.example.apisample.product.exception.ProductNotFoundException;
import com.example.apisample.product.model.dto.ProductCreateDTO;
import com.example.apisample.product.model.dto.ProductResponseDTO;
import com.example.apisample.product.model.dto.ProductUpdateDTO;
import com.example.apisample.product.model.mapper.ProductMapper;
import com.example.apisample.product.repository.ProductRepository;
import com.example.apisample.product.service.ProductService;
import com.example.apisample.utils.pagination.APIPageableResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public APIPageableResponseDTO<ProductResponseDTO> getALlProduct(int pageNo, int pageSize, String search, String sortField) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        Page<Product> page = productRepository.findByDeletedFalse(pageable);
        Page<ProductResponseDTO> productDtoPage = page.map(ProductMapper::productToDTO);

        return new APIPageableResponseDTO<>(productDtoPage);
    }

    @Override
    public ProductResponseDTO getProductById(Integer id) {
        Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);

        if (product.getDeleted()) {
            throw new ProductDeletedException();
        }

        return ProductMapper.productToDTO(product);
    }

    @Override
    @Transactional
    public void createProduct(ProductCreateDTO dto) {

        Product product = Product.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .featured(dto.getFeatured())
                .deleted(Boolean.FALSE)
                .build();

        productRepository.save(product);
    }

    @Override
    @Transactional
    public void updateProduct(Integer id, ProductUpdateDTO dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);

        if (product.getDeleted()) {
            throw new ProductDeletedException();
        }

        if (dto.getName() != null) product.setName(dto.getName());
        if (dto.getDescription() != null) product.setDescription(dto.getDescription());
        if (dto.getPrice() != null) product.setPrice(dto.getPrice());
        if (dto.getFeatured() != null) product.setFeatured(dto.getFeatured());

        productRepository.save(product);
    }

    @Override
    @Transactional
    public void deleteProduct(Integer id) {
        Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);

        product.setDeleted(Boolean.TRUE);

        productRepository.save(product);
    }

    @Override
    @Transactional
    public void restoreProduct(Integer id) {
        Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);

        if (!product.getDeleted()) {
            throw new ProductDeletedException();
        }

        product.setDeleted(Boolean.FALSE);

        productRepository.save(product);
    }
}
