package com.example.apisample.product.controller;

import com.example.apisample.product.model.dto.ProductCreateDTO;
import com.example.apisample.product.model.dto.ProductResponseDTO;
import com.example.apisample.product.model.dto.ProductUpdateDTO;
import com.example.apisample.product.service.ProductService;
import com.example.apisample.utils.ApiResponse;
import com.example.apisample.utils.message.LogMessage;
import com.example.apisample.utils.message.ResponseMessage;
import com.example.apisample.utils.pagination.APIPageableResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "http://localhost:5173", maxAge = 3600)
@Slf4j
public class ProductController {
    private final ProductService productService;

    final String DEFAULT_PAGE = "0";
    final String DEFAULT_PAGE_SIZE = "8";

    @GetMapping()
    public APIPageableResponseDTO<ProductResponseDTO> getAllProduct(@RequestParam(defaultValue = DEFAULT_PAGE, name = "page") Integer pageNo,
                                                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE, name = "size") Integer pageSize,
                                                              @RequestParam(defaultValue = "", name = "search") String search,
                                                              @RequestParam(defaultValue = "createOn", name= "sort") String sort) {
        return productService.getALlProduct(pageNo, pageSize, search, sort);
    }

    @GetMapping("/featured")
    public APIPageableResponseDTO<ProductResponseDTO> getAllFeaturedProduct(@RequestParam(defaultValue = DEFAULT_PAGE, name = "page") Integer pageNo,
                                                                    @RequestParam(defaultValue = DEFAULT_PAGE_SIZE, name = "size") Integer pageSize,
                                                                    @RequestParam(defaultValue = "", name = "search") String search,
                                                                    @RequestParam(defaultValue = "createOn", name= "sort") String sort) {
        return productService.getALlFeaturedProduct(pageNo, pageSize, search, sort);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Integer id) {
        log.debug(LogMessage.PRODUCT_GET_BY_ID_START);

        ProductResponseDTO product = productService.getProductById(id);

        log.info(LogMessage.PRODUCT_GET_BY_ID_SUCCESS);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(ResponseMessage.msgSuccess)
                        .data(product)
                        .build()
        );
    }

    @PostMapping("/admin")
    public ResponseEntity<ApiResponse> createProduct(@RequestBody @Valid ProductCreateDTO dto) {
        log.debug(LogMessage.PRODUCT_CREATE_START);

        productService.createProduct(dto);

        log.info(LogMessage.PRODUCT_CREATE_SUCCESS);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message(ResponseMessage.msgSuccess)
                        .build()
        );
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<ApiResponse> updateProduct(
            @PathVariable Integer id,
            @RequestBody @Valid ProductUpdateDTO dto) throws Exception {

        log.debug(LogMessage.PRODUCT_UPDATE_START);

        productService.updateProduct(id, dto);

        log.info(LogMessage.PRODUCT_UPDATE_SUCCESS);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(ResponseMessage.msgSuccess)
                        .build()
        );
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Integer id) {
        log.debug(LogMessage.PRODUCT_DELETE_START);

        productService.deleteProduct(id);

        log.info(LogMessage.PRODUCT_DELETE_SUCCESS);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(ResponseMessage.msgSuccess)
                        .build()
        );
    }

    @PostMapping("/admin/restore/{id}")
    public ResponseEntity<ApiResponse> restoreProduct(@PathVariable Integer id) {
        log.debug(LogMessage.PRODUCT_RESTORE_START);

        productService.restoreProduct(id);

        log.info(LogMessage.PRODUCT_RESTORE_SUCCESS);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(ResponseMessage.msgSuccess)
                        .build()
        );
    }

    @GetMapping("/get-image/{id}")
    public ResponseEntity<ApiResponse> getProductImage(@PathVariable("id") Integer productId) throws Exception{
        log.debug(LogMessage.PRODUCT_GET_IMAGE_START);

        ProductResponseDTO product = productService.getProductById(productId);

        log.info(LogMessage.PRODUCT_GET_IMAGE_SUCCESS);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(ResponseMessage.msgSuccess)
                        .data(product.getImage())
                        .build()
        );
    }

    @PostMapping(value = "/admin/image/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE )
    public ResponseEntity<ApiResponse> updateUserProductImage(@PathVariable("id") Integer productId, @RequestParam("file") MultipartFile file) throws Exception{
        log.debug(LogMessage.PRODUCT_UPLOAD_IMAGE_START);

        String imageURL = productService.uploadProductImage(productId, file);

        log.info(LogMessage.PRODUCT_UPLOAD_IMAGE_SUCCESS);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(ResponseMessage.msgSuccess)
                        .data(imageURL)
                        .build()
        );
    }

}
