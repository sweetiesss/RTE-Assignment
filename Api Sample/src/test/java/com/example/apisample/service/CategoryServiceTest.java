package com.example.apisample.service;

import com.example.apisample.category.entity.Category;
import com.example.apisample.category.exception.CategoryAlreadyExistsException;
import com.example.apisample.category.exception.CategoryNotFoundException;
import com.example.apisample.category.model.dto.CategoryRequestDTO;
import com.example.apisample.category.model.dto.CategoryResponseDTO;
import com.example.apisample.category.model.dto.CategoryUpdateRequestDTO;
import com.example.apisample.category.repository.CategoryRepository;
import com.example.apisample.category.service.impl.CategoryServiceImpl;
import com.example.apisample.utils.pagination.APIPageableResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;
    private CategoryRequestDTO requestDTO;
    private CategoryUpdateRequestDTO updateRequestDTO;

    @BeforeEach
    void setUp() {
        category = Category.builder()
                .id(1)
                .name("Electronics")
                .description("Devices and gadgets")
                .build();

        requestDTO = CategoryRequestDTO.builder()
                .name("Electronics")
                .description("Devices and gadgets")
                .build();

        updateRequestDTO = CategoryUpdateRequestDTO.builder()
                .name("Updated Name")
                .description("Updated Description")
                .build();
    }

    @Test
    void testCreateCategory_Success() {
        when(categoryRepository.existsByName(requestDTO.getName())).thenReturn(false);

        categoryService.createCategory(requestDTO);

        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void testCreateCategory_AlreadyExists() {
        when(categoryRepository.existsByName(requestDTO.getName())).thenReturn(true);

        assertThrows(CategoryAlreadyExistsException.class,
                () -> categoryService.createCategory(requestDTO));
    }

    @Test
    void testUpdateCategory_Success() {
        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));

        categoryService.updateCategory(1, updateRequestDTO);

        verify(categoryRepository, times(1)).save(category);
        assertEquals("Updated Name", category.getName());
        assertEquals("Updated Description", category.getDescription());
    }

    @Test
    void testUpdateCategory_NotFound() {
        when(categoryRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class,
                () -> categoryService.updateCategory(1, updateRequestDTO));
    }

    @Test
    void testDeleteCategory_Success() {
        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));

        categoryService.deleteCategory(1);

        verify(categoryRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteCategory_NotFound() {
        when(categoryRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class,
                () -> categoryService.deleteCategory(1));
    }

    @Test
    void testGetCategoryById_Success() {
        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));

        CategoryResponseDTO result = categoryService.getCategoryById(1);

        assertEquals(category.getId(), result.getId());
        assertEquals(category.getName(), result.getName());
        assertEquals(category.getDescription(), result.getDescription());
    }

    @Test
    void testGetCategoryById_NotFound() {
        when(categoryRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class,
                () -> categoryService.getCategoryById(1));
    }

    @Test
    void testGetAllCategories_WithSearchAndSort() {
        String search = "Electronics";
        String sort = "name";
        int pageNo = 0;
        int pageSize = 2;

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sort).ascending());
        Page<Category> mockPage = new PageImpl<>(List.of(category), pageable, 1);

        when(categoryRepository.findAllByNameContainingIgnoreCase(search, pageable))
                .thenReturn(mockPage);

        APIPageableResponseDTO<CategoryResponseDTO> response = categoryService.getAllCategories(pageNo, pageSize, search, sort);

        assertNotNull(response);
        assertEquals(1, response.getContent().size());
        assertEquals("Electronics", response.getContent().get(0).getName());
    }
}
