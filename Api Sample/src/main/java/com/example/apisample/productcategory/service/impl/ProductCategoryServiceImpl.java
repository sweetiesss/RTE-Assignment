package com.example.apisample.productcategory.service.impl;

import com.example.apisample.category.entity.Category;
import com.example.apisample.category.exception.CategoryNotFoundException;
import com.example.apisample.category.model.dto.CategoryResponseDTO;
import com.example.apisample.category.repository.CategoryRepository;
import com.example.apisample.product.entity.Product;
import com.example.apisample.product.exception.ProductNotFoundException;
import com.example.apisample.product.model.dto.ProductResponseDTO;
import com.example.apisample.product.repository.ProductRepository;
import com.example.apisample.productcategory.entity.ProductCategory;
import com.example.apisample.productcategory.entity.ProductCategoryId;
import com.example.apisample.productcategory.exception.ProductCategoryNotFoundException;
import com.example.apisample.productcategory.model.dto.ProductCategoryRequestDTO;
import com.example.apisample.productcategory.model.dto.ProductCategoryResponseDTO;
import com.example.apisample.productcategory.model.mapper.ProductCategoryMapper;
import com.example.apisample.productcategory.repository.ProductCategoryRepository;
import com.example.apisample.productcategory.service.ProductCategoryService;
import com.example.apisample.rating.service.RatingService;
import com.example.apisample.utils.pagination.APIPageableResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final RatingService ratingService;

    private static final String SORTFIELD_PRICE = "price";
    private static final String SORTFIELD_PRICE_DESC = "priceDesc";
    private static final String SORTFIELD_RATING = "rating";
    private static final String SORTFIELD_DEFAULT = "createOn";

    @Override
    @Transactional
    public void addCategoryToProduct(ProductCategoryRequestDTO requestDTO) {
        Product product = productRepository.findById(requestDTO.getProductId())
                .orElseThrow(ProductNotFoundException::new);

        // Step 1: Delete existing category relations for the product
        productCategoryRepository.deleteByProduct_Id(product.getId());

        // Step 2: Map the new category IDs to ProductCategory entities
        List<ProductCategory> productCategories = requestDTO.getCategoryId().stream()
                .map(categoryId -> {
                    Category category = categoryRepository.findById(categoryId)
                            .orElseThrow(CategoryNotFoundException::new);
                    return ProductCategory.builder()
                            .id(new ProductCategoryId(product.getId(), category.getId()))
                            .product(product)
                            .category(category)
                            .build();
                }).collect(Collectors.toList());

        // Step 3: Save the new category associations
        productCategoryRepository.saveAll(productCategories);
    }


    @Override
    @Transactional
    public void removeCategoryFromProduct(Integer productId, Integer categoryId) {
        ProductCategoryId id = new ProductCategoryId(productId, categoryId);
        ProductCategory productCategory = productCategoryRepository.findById(id)
                .orElseThrow(ProductCategoryNotFoundException::new);

        productCategoryRepository.delete(productCategory);
    }

    @Override
    @Transactional
    public APIPageableResponseDTO<ProductCategoryResponseDTO> getAllProductCategories(int pageNo, int pageSize, String sort, String search) {
        Pageable pageable = createPageable(pageNo, pageSize, sort);
        Page<ProductCategory> page = productCategoryRepository.findAll(pageable);
        List<ProductCategoryResponseDTO> dtoList = mergeProductCategories(page.getContent());
        return wrapInPageableResponse(dtoList, pageable, page.getTotalElements());
    }

    @Override
    @Transactional
    public APIPageableResponseDTO<ProductResponseDTO> getProductsByCategory(
            Integer categoryId, int pageNo, int pageSize, String sortField, String search) {

        Pageable pageable = PageRequest.of(pageNo, pageSize); // sorting will be manual
        List<ProductCategory> productCategories = productCategoryRepository.findAllByCategory_Id(categoryId);

        List<ProductResponseDTO> dtoList = productCategories.stream()
                .map(ProductCategory::getProduct)
                .filter(product -> {
                    if (search == null || search.isBlank()) return true;
                    return product.getName().toLowerCase().contains(search.toLowerCase());
                })
                .map(product -> ProductResponseDTO.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .description(product.getDescription())
                        .price(product.getPrice())
                        .image(product.getImage())
                        .featured(product.getFeatured())
                        .createOn(product.getCreateOn())
                        .lastUpdateOn(product.getLastUpdateOn())
                        .averageRating(ratingService.calculateAverageRating(product.getId()))
                        .build())
                .distinct()
                .collect(Collectors.toList());

        switch (sortField) {
            case SORTFIELD_PRICE -> dtoList.sort(Comparator.comparing(ProductResponseDTO::getPrice));
            case SORTFIELD_PRICE_DESC -> dtoList.sort(Comparator.comparing(ProductResponseDTO::getPrice).reversed());
            case SORTFIELD_RATING -> dtoList.sort((a, b) -> Double.compare(
                    b.getAverageRating() != null ? b.getAverageRating() : 0.0,
                    a.getAverageRating() != null ? a.getAverageRating() : 0.0
            ));
            case SORTFIELD_DEFAULT -> dtoList.sort(Comparator.comparing(ProductResponseDTO::getCreateOn).reversed());
            default -> {}
        }

        int start = Math.min(pageNo * pageSize, dtoList.size());
        int end = Math.min(start + pageSize, dtoList.size());
        List<ProductResponseDTO> paginatedList = dtoList.subList(start, end);

        Page<ProductResponseDTO> dtoPage = new PageImpl<>(paginatedList, pageable, dtoList.size());
        return new APIPageableResponseDTO<>(dtoPage);
    }


    @Override
    @Transactional
    public ProductCategoryResponseDTO getProductCategoriesByProductId(Integer productId) {
        List<ProductCategory> productCategories = productCategoryRepository.findAllByProduct_Id(productId);

        if (productCategories.isEmpty()) {
            throw new ProductCategoryNotFoundException();
        }

        ProductCategoryResponseDTO dto = ProductCategoryMapper.productCategoryToDTO(productCategories.getFirst());
        dto.setCategories(productCategories.stream()
                .map(this::mapCategoryToDTO)
                .collect(Collectors.toList()));
        return dto;
    }


    private Pageable createPageable(int pageNo, int pageSize, String sort) {
        return PageRequest.of(pageNo, pageSize, Sort.by(sort).ascending());
    }

    private APIPageableResponseDTO<ProductCategoryResponseDTO> wrapInPageableResponse(List<ProductCategoryResponseDTO> dtoList, Pageable pageable, long total) {
        Page<ProductCategoryResponseDTO> dtoPage = new PageImpl<>(dtoList, pageable, total);
        return new APIPageableResponseDTO<>(dtoPage);
    }

    private List<ProductCategoryResponseDTO> mergeProductCategories(List<ProductCategory> productCategories) {
        Map<Integer, ProductCategoryResponseDTO> productMap = new HashMap<>();

        for (ProductCategory pc : productCategories) {
            int productId = pc.getProduct().getId();

            productMap.computeIfAbsent(productId, id -> {
                ProductCategoryResponseDTO dto = ProductCategoryMapper.productCategoryToDTO(pc);
                dto.setCategories(new ArrayList<>());
                return dto;
            });

            productMap.get(productId).getCategories().add(mapCategoryToDTO(pc));
        }

        return new ArrayList<>(productMap.values());
    }

    private CategoryResponseDTO mapCategoryToDTO(ProductCategory pc) {
        return CategoryResponseDTO.builder()
                .id(pc.getCategory().getId())
                .name(pc.getCategory().getName())
                .description(pc.getCategory().getDescription())
                .build();
    }
}
