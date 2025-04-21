package com.example.apisample.service.Implement;

import com.example.apisample.entity.Category;
import com.example.apisample.entity.Product;
import com.example.apisample.entity.ProductCategory;
import com.example.apisample.entity.ProductCategoryId;
import com.example.apisample.exception.categoryservice.CategoryNotFoundException;
import com.example.apisample.exception.productcategoryservice.ProductCategoryNotFoundException;
import com.example.apisample.exception.productservice.ProductNotFoundException;
import com.example.apisample.model.dto.category.CategoryResponseDTO;
import com.example.apisample.model.dto.pagination.APIPageableResponseDTO;
import com.example.apisample.model.dto.productcategory.ProductCategoryRequestDTO;
import com.example.apisample.model.dto.productcategory.ProductCategoryResponseDTO;
import com.example.apisample.model.mapper.ProductCategoryMapper;
import com.example.apisample.repository.CategoryRepository;
import com.example.apisample.repository.ProductCategoryRepository;
import com.example.apisample.repository.ProductRepository;
import com.example.apisample.service.Interface.ProductCategoryService;
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
public class ProductCategoryServiceImp implements ProductCategoryService {
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;


    public void addCategoryToProduct(ProductCategoryRequestDTO requestDTO) throws ProductNotFoundException, CategoryNotFoundException {
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
    public void removeCategoryFromProduct(Integer productId, Integer categoryId) throws ProductCategoryNotFoundException {
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

    public ProductCategoryResponseDTO getProductCategoriesByProductId(Integer productId) throws ProductCategoryNotFoundException {


        List<ProductCategory> productCategories = productCategoryRepository.findAllByProduct_Id(productId);

        if (productCategories.isEmpty()) {
            throw new ProductCategoryNotFoundException();
        }

        // Create a response DTO for the product
        ProductCategoryResponseDTO responseDTO = new ProductCategoryResponseDTO();
        responseDTO.setProductId(productId);
        responseDTO.setProductName(productCategories.get(0).getProduct().getName());

        // Aggregate all categories for the product
        List<CategoryResponseDTO> categories = new ArrayList<>();
        for (ProductCategory productCategory : productCategories) {
            categories.add(CategoryResponseDTO.builder()
                    .id(productCategory.getCategory().getId())
                    .name(productCategory.getCategory().getName())
                    .description(productCategory.getCategory().getDescription())
                    .build());
        }

        // Set the categories in the response DTO
        responseDTO.setCategories(categories);

        return responseDTO;
    }


}
