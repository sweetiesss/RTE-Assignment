package com.example.apisample.productcategory.controller;

import com.example.apisample.category.exception.CategoryNotFoundException;
import com.example.apisample.product.exception.ProductNotFoundException;
import com.example.apisample.productcategory.exception.ProductCategoryNotFoundException;
import com.example.apisample.productcategory.model.dto.ProductCategoryRequestDTO;
import com.example.apisample.productcategory.model.dto.ProductCategoryResponseDTO;
import com.example.apisample.productcategory.service.ProductCategoryService;
import com.example.apisample.utils.ResponseObject;
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
@RequestMapping("/product-categories")
@CrossOrigin(origins = "*")
@Slf4j
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;


    final String DEFAULT_PAGE = "0";
    final String DEFAULT_PAGE_SIZE = "8";

    @PostMapping("/admin")
    public ResponseEntity<ResponseObject> addCategoryToProduct(@RequestBody @Valid ProductCategoryRequestDTO dto) {
        log.info(LogMessage.logStartCreateProductCategory);
        productCategoryService.addCategoryToProduct(dto);
        log.info(LogMessage.logSuccessCreateProductCategory);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseObject.builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message(ResponseMessage.msgSuccess)
                        .build()
                );
    }

    @DeleteMapping("/admin/product/{productId}/category/{categoryId}")
    public ResponseEntity<ResponseObject> removeCategoryFromProduct(@PathVariable Integer productId, @PathVariable Integer categoryId) {
        log.info(LogMessage.logStartDeleteProductCategory);

        productCategoryService.removeCategoryFromProduct(productId, categoryId);

        log.info(LogMessage.logSuccessDeleteProductCategory);

        return ResponseEntity.ok(
                ResponseObject.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(ResponseMessage.msgSuccess)
                        .build()
        );
    }

    @GetMapping()
    public APIPageableResponseDTO<ProductCategoryResponseDTO> getAllCategory(@RequestParam(defaultValue = DEFAULT_PAGE, name = "page") Integer pageNo,
                                                                      @RequestParam(defaultValue = DEFAULT_PAGE_SIZE, name = "size") Integer pageSize,
                                                                      @RequestParam(defaultValue = "", name = "search") String search,
                                                                      @RequestParam(defaultValue = "", name = "sort") String sort) throws ProductNotFoundException {
        return productCategoryService.getAllProductCategories(pageNo, pageSize, search, sort);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductCategoryResponseDTO> getProductCategoryByIds(@PathVariable Integer productId) {
        log.info(LogMessage.logStartGetProductCategoryByIds);

        ProductCategoryResponseDTO productCategory = productCategoryService.getProductCategoriesByProductId(productId);

        log.info(LogMessage.logSuccessGetProductCategoryByIds);

        return ResponseEntity.ok(productCategory);
    }
}
