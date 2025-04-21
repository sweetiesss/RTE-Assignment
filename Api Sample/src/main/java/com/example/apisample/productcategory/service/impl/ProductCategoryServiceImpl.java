package com.example.apisample.productcategory.service.impl;

import com.example.apisample.category.entity.Category;
import com.example.apisample.category.exception.CategoryNotFoundException;
import com.example.apisample.category.model.dto.CategoryResponseDTO;
import com.example.apisample.category.repository.CategoryRepository;
import com.example.apisample.product.entity.Product;
import com.example.apisample.product.exception.ProductNotFoundException;
import com.example.apisample.product.repository.ProductRepository;
import com.example.apisample.productcategory.entity.ProductCategory;
import com.example.apisample.productcategory.entity.ProductCategoryId;
import com.example.apisample.productcategory.exception.ProductCategoryNotFoundException;
import com.example.apisample.productcategory.model.dto.ProductCategoryRequestDTO;
import com.example.apisample.productcategory.model.dto.ProductCategoryResponseDTO;
import com.example.apisample.productcategory.repository.ProductCategoryRepository;
import com.example.apisample.productcategory.service.ProductCategoryService;
import com.example.apisample.utils.pagination.APIPageableResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public void addCategoryToProduct(ProductCategoryRequestDTO requestDTO) {
        Product product = productRepository.findById(requestDTO.getProductId())
                .orElseThrow(ProductNotFoundException::new);

        List<ProductCategory> productCategoryList = new ArrayList<>();
        for(Integer categoryId : requestDTO.getCategoryId()) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(CategoryNotFoundException::new);

            ProductCategory productCategory = ProductCategory.builder()
                    .id(new ProductCategoryId(requestDTO.getProductId(), categoryId))
                    .product(product)
                    .category(category)
                    .build();

            productCategoryList.add(productCategory);
        }

        productCategoryRepository.saveAll(productCategoryList);
    }

    @Override
    @Transactional
    public void removeCategoryFromProduct(Integer productId, Integer categoryId) {
        ProductCategoryId productCategoryId = new ProductCategoryId(productId, categoryId);
        ProductCategory productCategory = productCategoryRepository.findById(productCategoryId)
                .orElseThrow(ProductCategoryNotFoundException::new);

        productCategoryRepository.delete(productCategory);
    }

    @Override
    @Transactional
    public APIPageableResponseDTO<ProductCategoryResponseDTO> getAllProductCategories(int pageNo, int pageSize, String search, String sort) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        Page<ProductCategory> page = productCategoryRepository.findAll(pageable);

        Map<Integer, ProductCategoryResponseDTO> productMap = new HashMap<>();

        for (ProductCategory productCategory : page.getContent()) {
            ProductCategoryResponseDTO responseDTO = productMap.computeIfAbsent(
                    productCategory.getProduct().getId(),
                    id -> new ProductCategoryResponseDTO() {{
                        setProductId(id);
                        setProductName(productCategory.getProduct().getName());
                        setCategories(new ArrayList<>());
                    }}
            );

            responseDTO.getCategories().add(CategoryResponseDTO.builder()
                    .id(productCategory.getCategory().getId())
                    .name(productCategory.getCategory().getName())
                    .description(productCategory.getCategory().getDescription())
                    .build());
        }

        List<ProductCategoryResponseDTO> productCategoryList = new ArrayList<>(productMap.values());

        Page<ProductCategoryResponseDTO> dtoPage = new PageImpl<>(productCategoryList, pageable, page.getTotalElements());

        return new APIPageableResponseDTO<>(dtoPage);
    }

    @Override
    @Transactional
    public ProductCategoryResponseDTO getProductCategoriesByProductId(Integer productId) {
        List<ProductCategory> productCategories = productCategoryRepository.findAllByProduct_Id(productId);

        if (productCategories.isEmpty()) {
            throw new ProductCategoryNotFoundException();
        }

        ProductCategoryResponseDTO responseDTO = new ProductCategoryResponseDTO();
        responseDTO.setProductId(productId);
        responseDTO.setProductName(productCategories.get(0).getProduct().getName());

        List<CategoryResponseDTO> categories = new ArrayList<>();
        for (ProductCategory productCategory : productCategories) {
            categories.add(CategoryResponseDTO.builder()
                    .id(productCategory.getCategory().getId())
                    .name(productCategory.getCategory().getName())
                    .description(productCategory.getCategory().getDescription())
                    .build());
        }

        responseDTO.setCategories(categories);

        return responseDTO;
    }


}
