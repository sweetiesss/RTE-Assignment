package com.example.apisample.controller;

import com.example.apisample.model.ResponseObject;
import com.example.apisample.model.dto.message.LogMessage;
import com.example.apisample.model.dto.message.ResponseMessage;
import com.example.apisample.model.dto.pagination.APIPageableResponseDTO;
import com.example.apisample.model.dto.product.ProductCreateDTO;
import com.example.apisample.model.dto.product.ProductResponseDTO;
import com.example.apisample.model.dto.product.ProductUpdateDTO;
import com.example.apisample.model.dto.user.UserResponseDTO;
import com.example.apisample.service.Interface.ProductService;
import com.example.apisample.service.Interface.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "*")
@Slf4j
public class ProductController {
    private final ProductService productService;

    final String DEFAULT_PAGE = "0";
    final String DEFAULT_PAGE_SIZE = "8";

    @GetMapping("/get-all")
    public APIPageableResponseDTO<ProductResponseDTO> getUser(@RequestParam(defaultValue = DEFAULT_PAGE, name = "page") Integer pageNo,
                                                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE, name = "size") Integer pageSize,
                                                              @RequestParam(defaultValue = "", name = "search") String search,
                                                              @RequestParam(defaultValue = "id", name= "sort") String sort) {
        return productService.getALlProduct(pageNo,pageSize,search,sort);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ResponseObject> getProductById(@PathVariable Integer id) throws Exception {
        log.info(LogMessage.logStartGetProductById);

        ProductResponseDTO product = productService.getProductById(id);

        log.info(LogMessage.logSuccessGetProductById);

        return ResponseEntity.ok(
                ResponseObject.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(ResponseMessage.msgSuccess)
                        .token(product)
                        .build()
        );
    }

    @PostMapping("/admin/create")
    public ResponseEntity<ResponseObject> createProduct(@RequestBody @Valid ProductCreateDTO dto) {
        log.info(LogMessage.logStartCreateProduct);

        productService.createProduct(dto);

        log.info(LogMessage.logSuccessCreateProduct);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ResponseObject.builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message(ResponseMessage.msgSuccess)
                        .build()
        );
    }

    @PutMapping("/admin/update/{id}")
    public ResponseEntity<ResponseObject> updateProduct(
            @PathVariable Integer id,
            @RequestBody @Valid ProductUpdateDTO dto) throws Exception {

        log.info(LogMessage.logStartUpdateProduct);

        productService.updateProduct(id, dto);

        log.info(LogMessage.logSuccessUpdateProduct);

        return ResponseEntity.ok(
                ResponseObject.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(ResponseMessage.msgSuccess)
                        .build()
        );
    }

    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<ResponseObject> deleteProduct(@PathVariable Integer id) throws Exception {
        log.info(LogMessage.logStartDeleteProduct);

        productService.deleteProduct(id);

        log.info(LogMessage.logSuccessDeleteProduct);

        return ResponseEntity.ok(
                ResponseObject.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(ResponseMessage.msgSuccess)
                        .build()
        );
    }

    @PostMapping("/admin/restore/{id}")
    public ResponseEntity<ResponseObject> restoreProduct(@PathVariable Integer id) throws Exception {
        log.info(LogMessage.logStartRestoreProduct);

        productService.restoreProduct(id);

        log.info(LogMessage.logSuccessRestoreProduct);

        return ResponseEntity.ok(
                ResponseObject.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(ResponseMessage.msgSuccess)
                        .build()
        );
    }

}
