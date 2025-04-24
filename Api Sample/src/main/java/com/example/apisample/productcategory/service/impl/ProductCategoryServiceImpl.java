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
import com.example.apisample.productcategory.model.mapper.ProductCategoryMapper;
import com.example.apisample.productcategory.repository.ProductCategoryRepository;
import com.example.apisample.productcategory.service.ProductCategoryService;
import com.example.apisample.utils.pagination.APIPageableResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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
        Pageable pageable = createPageable(pageNo, pageSize, sort);

        Page<ProductCategory> page = productCategoryRepository.findAll(pageable);

        List<ProductCategoryResponseDTO> dtoList = mergeProductCategories(page.getContent());

        Page<ProductCategoryResponseDTO> dtoPage = new PageImpl<>(dtoList, pageable, page.getTotalElements());
        return new APIPageableResponseDTO<>(dtoPage);
    }

    private Pageable createPageable(int pageNo, int pageSize, String sort) {
        return PageRequest.of(pageNo, pageSize, Sort.by(sort).ascending());
    }

    private List<ProductCategoryResponseDTO> mergeProductCategories(List<ProductCategory> productCategories) {
        Map<Integer, ProductCategoryResponseDTO> productMap = new HashMap<>();

        productCategories.forEach(productCategory -> {
            int productId = productCategory.getProduct().getId();

            productMap.computeIfAbsent(productId, id -> {
                ProductCategoryResponseDTO dto = ProductCategoryMapper.productCategoryToDTO(productCategory);
                dto.setCategories(new ArrayList<>()); // Reset to avoid duplicate first category
                return dto;
            });

            productMap.get(productId).getCategories().add(CategoryResponseDTO.builder()
                    .id(productCategory.getCategory().getId())
                    .name(productCategory.getCategory().getName())
                    .description(productCategory.getCategory().getDescription())
                    .build());
        });

        return new ArrayList<>(productMap.values());
    }


    @Override
    @Transactional
    public ProductCategoryResponseDTO getProductCategoriesByProductId(Integer productId) {
        List<ProductCategory> productCategories = productCategoryRepository.findAllByProduct_Id(productId);

        if (productCategories.isEmpty()) {
            throw new ProductCategoryNotFoundException();
        }

        ProductCategoryResponseDTO responseDTO = ProductCategoryMapper.productCategoryToDTO(productCategories.get(0));

        List<CategoryResponseDTO> categories = productCategories.stream()
                .map(pc -> CategoryResponseDTO.builder()
                        .id(pc.getCategory().getId())
                        .name(pc.getCategory().getName())
                        .description(pc.getCategory().getDescription())
                        .build())
                .toList();

        responseDTO.setCategories(categories);

        return responseDTO;
    }

}
