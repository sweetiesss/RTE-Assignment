package com.example.apisample.controller;

import com.example.apisample.category.controller.CategoryController;
import com.example.apisample.category.model.dto.CategoryRequestDTO;
import com.example.apisample.category.model.dto.CategoryResponseDTO;
import com.example.apisample.category.model.dto.CategoryUpdateRequestDTO;
import com.example.apisample.category.service.CategoryService;
import com.example.apisample.utils.message.ResponseMessage;
import com.example.apisample.utils.pagination.APIPageableResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CategoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetCategoryById() throws Exception {
        // Prepare data
        CategoryResponseDTO category = CategoryResponseDTO.builder()
                .id(1)
                .name("Category 1")
                .description("Description of Category 1")
                .build();

        // Mock the service call
        when(categoryService.getCategoryById(1)).thenReturn(category);

        // Perform the request and verify the result
        MvcResult result = mockMvc.perform(get("/categories/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value(ResponseMessage.msgSuccess))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Category 1"))
                .andExpect(jsonPath("$.data.description").value("Description of Category 1"))
                .andReturn();

        // Verify service interaction
        verify(categoryService, times(1)).getCategoryById(1);
    }

    @Test
    void testCreateCategory() throws Exception {
        // Prepare the request body
        CategoryRequestDTO categoryRequest = CategoryRequestDTO.builder()
                .name("New Category")
                .description("New category description")
                .build();

        // Perform the request and verify the result
        mockMvc.perform(post("/admin/categories")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(categoryRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.message").value(ResponseMessage.msgSuccess));

        // Verify service interaction
        verify(categoryService, times(1)).createCategory(categoryRequest);
    }

    @Test
    void testUpdateCategory() throws Exception {
        // Prepare the request body
        CategoryUpdateRequestDTO categoryUpdateRequest = CategoryUpdateRequestDTO.builder()
                .name("Updated Category")
                .description("Updated description")
                .build();

        // Perform the request and verify the result
        mockMvc.perform(put("/admin/categories/{id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(categoryUpdateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value(ResponseMessage.msgSuccess));

        // Verify service interaction
        verify(categoryService, times(1)).updateCategory(1, categoryUpdateRequest);
    }

    @Test
    void testDeleteCategory() throws Exception {
        // Perform the request and verify the result
        mockMvc.perform(delete("/admin/categories/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value(ResponseMessage.msgSuccess));

        // Verify service interaction
        verify(categoryService, times(1)).deleteCategory(1);
    }


    @Test
    public void testGetAllCategories() {
        CategoryResponseDTO category1 = CategoryResponseDTO.builder().id(1).name("Category 1").description("Description 1").build();
        CategoryResponseDTO category2 = CategoryResponseDTO.builder().id(2).name("Category 2").description("Description 2").build();

        // Mock a Page object using PageImpl
        PageRequest pageRequest = PageRequest.of(0, 8);
        Page<CategoryResponseDTO> page = new PageImpl<>(List.of(category1, category2), pageRequest, 2);

        // Mock service call
        when(categoryService.getAllCategories(anyInt(), anyInt(), anyString(), anyString())).thenReturn(
                new APIPageableResponseDTO<>(page)
        );

        // Call the controller method
        APIPageableResponseDTO<CategoryResponseDTO> response = categoryController.getAllCategory(0, 8, "", "id");

        // Assertions
        assertEquals(2, response.getContent().size());
        assertEquals("Category 1", response.getContent().get(0).getName());
    }
}
