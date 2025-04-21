package com.example.apisample.controller;

import com.example.apisample.model.ResponseObject;
import com.example.apisample.model.dto.category.CategoryRequestDTO;
import com.example.apisample.model.dto.category.CategoryResponseDTO;
import com.example.apisample.model.dto.category.CategoryUpdateRequestDTO;
import com.example.apisample.model.dto.message.LogMessage;
import com.example.apisample.model.dto.message.ResponseMessage;
import com.example.apisample.model.dto.pagination.APIPageableResponseDTO;
import com.example.apisample.service.Interface.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
@CrossOrigin(origins = "*")
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    final String DEFAULT_PAGE = "0";
    final String DEFAULT_PAGE_SIZE = "8";

    @GetMapping("/get-all")
    public APIPageableResponseDTO<CategoryResponseDTO> getAllCategory(@RequestParam(defaultValue = DEFAULT_PAGE, name = "page") Integer pageNo,
                                                                      @RequestParam(defaultValue = DEFAULT_PAGE_SIZE, name = "size") Integer pageSize,
                                                                      @RequestParam(defaultValue = "", name = "search") String search,
                                                                      @RequestParam(defaultValue = "", name = "sort") String sort) {
        return categoryService.getAllCategories(pageNo, pageSize, search, sort);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ResponseObject> getCategoryById(@PathVariable Integer id) throws Exception {
        log.info(LogMessage.logStartGetCategoryById);

        CategoryResponseDTO category = categoryService.getCategoryById(id);

        log.info(LogMessage.logSuccessGetCategoryById);

        return ResponseEntity.ok(
                ResponseObject.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(ResponseMessage.msgSuccess)
                        .token(category)
                        .build()
        );
    }

    @PostMapping("/admin/create")
    public ResponseEntity<ResponseObject> createCategory(@RequestBody @Valid CategoryRequestDTO dto) throws Exception {
        log.info(LogMessage.logStartCreateCategory);

        categoryService.createCategory(dto);

        log.info(LogMessage.logSuccessCreateCategory);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseObject.builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message(ResponseMessage.msgSuccess)
                        .build()
                );
    }

    @PutMapping("/admin/update/{id}")
    public ResponseEntity<ResponseObject> updateCategory(@PathVariable Integer id, @RequestBody @Valid CategoryUpdateRequestDTO dto) throws Exception {
        log.info(LogMessage.logStartUpdateCategory);

        categoryService.updateCategory(id, dto);

        log.info(LogMessage.logSuccessUpdateCategory);

        return ResponseEntity.ok(
                ResponseObject.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(ResponseMessage.msgSuccess)
                        .build()
        );
    }

    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<ResponseObject> deleteCategory(@PathVariable Integer id) throws Exception {
        log.info(LogMessage.logStartDeleteCategory);

        categoryService.deleteCategory(id);

        log.info(LogMessage.logSuccessDeleteCategory);

        return ResponseEntity.ok(
                ResponseObject.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(ResponseMessage.msgSuccess)
                        .build()
        );
    }
}
