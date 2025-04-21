package com.example.apisample.controller;

import com.example.apisample.exception.categoryservice.CategoryNotFoundException;
import com.example.apisample.exception.productcategoryservice.ProductCategoryNotFoundException;
import com.example.apisample.exception.productservice.ProductNotFoundException;
import com.example.apisample.model.ResponseObject;
import com.example.apisample.model.dto.pagination.APIPageableResponseDTO;
import com.example.apisample.model.dto.productcategory.ProductCategoryRequestDTO;
import com.example.apisample.model.dto.productcategory.ProductCategoryResponseDTO;
import com.example.apisample.model.dto.message.LogMessage;
import com.example.apisample.model.dto.message.ResponseMessage;
import com.example.apisample.service.Interface.ProductCategoryService;
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

    @PostMapping("/admin/create")
    public ResponseEntity<ResponseObject> addCategoryToProduct(@RequestBody ProductCategoryRequestDTO dto) throws CategoryNotFoundException, ProductNotFoundException {
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

    @DeleteMapping("/admin/delete/{productId}/{categoryId}")
    public ResponseEntity<ResponseObject> removeCategoryFromProduct(@PathVariable Integer productId, @PathVariable Integer categoryId) throws ProductCategoryNotFoundException {
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

    @GetMapping("/get-all")
    public APIPageableResponseDTO<ProductCategoryResponseDTO> getAllCategory(@RequestParam(defaultValue = DEFAULT_PAGE, name = "page") Integer pageNo,
                                                                      @RequestParam(defaultValue = DEFAULT_PAGE_SIZE, name = "size") Integer pageSize,
                                                                      @RequestParam(defaultValue = "", name = "search") String search,
                                                                      @RequestParam(defaultValue = "", name = "sort") String sort) throws ProductNotFoundException {
        return productCategoryService.getAllProductCategories(pageNo, pageSize, search, sort);
    }

    @GetMapping("/get/{productId}")
    public ResponseEntity<ProductCategoryResponseDTO> getProductCategoryByIds(@PathVariable Integer productId) throws ProductCategoryNotFoundException {
        log.info(LogMessage.logStartGetProductCategoryByIds);

        ProductCategoryResponseDTO productCategory = productCategoryService.getProductCategoriesByProductId(productId);

        log.info(LogMessage.logSuccessGetProductCategoryByIds);

        return ResponseEntity.ok(productCategory);
    }
}
