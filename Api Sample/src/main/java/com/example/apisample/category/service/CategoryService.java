package com.example.apisample.category.service;

import com.example.apisample.category.model.dto.CategoryRequestDTO;
import com.example.apisample.category.model.dto.CategoryResponseDTO;
import com.example.apisample.category.model.dto.CategoryUpdateRequestDTO;
import com.example.apisample.utils.pagination.APIPageableResponseDTO;

public interface CategoryService {
    void createCategory(CategoryRequestDTO dto) ;

    void updateCategory(Integer id, CategoryUpdateRequestDTO dto) ;

    void deleteCategory(Integer id) ;

    CategoryResponseDTO getCategoryById(Integer id) ;

    APIPageableResponseDTO<CategoryResponseDTO> getAllCategories(int pageNo, int pageSize, String search, String sort);
}
