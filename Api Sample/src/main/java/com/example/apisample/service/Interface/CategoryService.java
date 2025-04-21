package com.example.apisample.service.Interface;

import com.example.apisample.model.dto.category.CategoryRequestDTO;
import com.example.apisample.model.dto.category.CategoryResponseDTO;
import com.example.apisample.model.dto.category.CategoryUpdateRequestDTO;
import com.example.apisample.model.dto.pagination.APIPageableResponseDTO;

public interface CategoryService {
    void createCategory(CategoryRequestDTO dto) throws Exception;

    void updateCategory(Integer id, CategoryUpdateRequestDTO dto) throws Exception;

    void deleteCategory(Integer id) throws Exception;

    CategoryResponseDTO getCategoryById(Integer id) throws Exception;

    APIPageableResponseDTO<CategoryResponseDTO> getAllCategories(int pageNo, int pageSize, String search, String sort);
}
