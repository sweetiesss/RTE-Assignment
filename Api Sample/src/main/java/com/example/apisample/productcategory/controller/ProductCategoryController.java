package com.example.apisample.productcategory.controller;

import com.example.apisample.product.exception.ProductNotFoundException;
import com.example.apisample.product.model.dto.ProductResponseDTO;
import com.example.apisample.productcategory.model.dto.ProductCategoryRequestDTO;
import com.example.apisample.productcategory.model.dto.ProductCategoryResponseDTO;
import com.example.apisample.productcategory.service.ProductCategoryService;
import com.example.apisample.utils.ApiResponse;
import com.example.apisample.utils.message.LogMessage;
import com.example.apisample.utils.message.ResponseMessage;
import com.example.apisample.utils.pagination.APIPageableResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*")
@Slf4j
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;

    final String DEFAULT_PAGE = "0";
    final String DEFAULT_PAGE_SIZE = "8";

    @PostMapping("/admin/product-categories")
    public ResponseEntity<ApiResponse> addCategoryToProduct(@RequestBody @Valid ProductCategoryRequestDTO dto) {
        log.debug(LogMessage.PRODUCT_CATEGORY_CREATE_START);

        productCategoryService.addCategoryToProduct(dto);

        log.info(LogMessage.PRODUCT_CATEGORY_CREATE_SUCCESS);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message(ResponseMessage.msgSuccess)
                        .build()
                );
    }

    @GetMapping("/product-categories")
    public APIPageableResponseDTO<ProductCategoryResponseDTO> getAllCategory(@RequestParam(defaultValue = DEFAULT_PAGE, name = "page") Integer pageNo,
                                                                             @RequestParam(defaultValue = DEFAULT_PAGE_SIZE, name = "size") Integer pageSize,
                                                                             @RequestParam(defaultValue = "", name = "search") String search,
                                                                             @RequestParam(defaultValue = "Product", name = "sort") String sort) throws ProductNotFoundException {
        log.debug(LogMessage.PRODUCT_CATEGORY_GET_ALL_START);

        APIPageableResponseDTO<ProductCategoryResponseDTO> result = productCategoryService.getAllProductCategories(pageNo, pageSize, search, sort);

        log.info(LogMessage.PRODUCT_CATEGORY_GET_ALL_SUCCESS);

        return result;
    }

    @GetMapping("/product-categories/{productId}")
    public ResponseEntity<ProductCategoryResponseDTO> getProductCategoryByIds(@PathVariable Integer productId) {
        log.debug(LogMessage.PRODUCT_CATEGORY_GET_BY_IDS_START);

        ProductCategoryResponseDTO productCategory = productCategoryService.getProductCategoriesByProductId(productId);

        log.info(LogMessage.PRODUCT_CATEGORY_GET_BY_IDS_SUCCESS);

        return ResponseEntity.ok(productCategory);
    }

}
