package com.example.apisample.category.controller;

import com.example.apisample.category.model.dto.CategoryRequestDTO;
import com.example.apisample.category.model.dto.CategoryResponseDTO;
import com.example.apisample.category.model.dto.CategoryUpdateRequestDTO;
import com.example.apisample.category.service.CategoryService;
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
@RequestMapping("/categories")
@CrossOrigin(origins = "*")
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    final String DEFAULT_PAGE = "0";
    final String DEFAULT_PAGE_SIZE = "8";

    @GetMapping()
    public APIPageableResponseDTO<CategoryResponseDTO> getAllCategory(@RequestParam(defaultValue = DEFAULT_PAGE, name = "page") Integer pageNo,
                                                                      @RequestParam(defaultValue = DEFAULT_PAGE_SIZE, name = "size") Integer pageSize,
                                                                      @RequestParam(defaultValue = "", name = "search") String search,
                                                                      @RequestParam(defaultValue = "id", name = "sort") String sort) {
        return categoryService.getAllCategories(pageNo, pageSize, search, sort);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Integer id) {
        log.debug(LogMessage.CATEGORY_GET_BY_ID_START);

        CategoryResponseDTO category = categoryService.getCategoryById(id);

        log.info(LogMessage.CATEGORY_GET_BY_ID_SUCCESS);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(ResponseMessage.msgSuccess)
                        .data(category)
                        .build()
        );
    }

    @PostMapping("/admin/create")
    public ResponseEntity<ApiResponse> createCategory(@RequestBody @Valid CategoryRequestDTO dto) {
        log.debug(LogMessage.CATEGORY_CREATE_START);

        categoryService.createCategory(dto);

        log.info(LogMessage.CATEGORY_CREATE_SUCCESS);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message(ResponseMessage.msgSuccess)
                        .build()
                );
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable Integer id, @RequestBody @Valid CategoryUpdateRequestDTO dto) {
        log.debug(LogMessage.CATEGORY_UPDATE_START);

        categoryService.updateCategory(id, dto);

        log.info(LogMessage.CATEGORY_UPDATE_SUCCESS);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(ResponseMessage.msgSuccess)
                        .build()
        );
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Integer id) {
        log.debug(LogMessage.CATEGORY_DELETE_START);

        categoryService.deleteCategory(id);

        log.info(LogMessage.CATEGORY_DELETE_SUCCESS);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(ResponseMessage.msgSuccess)
                        .build()
        );
    }
}
