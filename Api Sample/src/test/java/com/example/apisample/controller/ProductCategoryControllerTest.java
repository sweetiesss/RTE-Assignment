package com.example.apisample.controller;

import com.example.apisample.category.model.dto.CategoryResponseDTO;
import com.example.apisample.productcategory.controller.ProductCategoryController;
import com.example.apisample.productcategory.model.dto.ProductCategoryRequestDTO;
import com.example.apisample.productcategory.model.dto.ProductCategoryResponseDTO;
import com.example.apisample.productcategory.service.ProductCategoryService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductCategoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductCategoryService productCategoryService;

    @InjectMocks
    private ProductCategoryController productCategoryController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productCategoryController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testAddCategoryToProduct() throws Exception {
        // Prepare the request body
        ProductCategoryRequestDTO productCategoryRequest = ProductCategoryRequestDTO.builder()
                .productId(1)
                .categoryId(List.of(1, 2))
                .build();

        // Perform the request and verify the result
        mockMvc.perform(post("/admin/product-categories")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(productCategoryRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.message").value(ResponseMessage.msgSuccess));

        // Verify service interaction
        verify(productCategoryService, times(1)).addCategoryToProduct(productCategoryRequest);
    }

    @Test
    void testGetAllCategories() throws Exception {
        ProductCategoryResponseDTO responseDTO = ProductCategoryResponseDTO.builder()
                .productId(1)
                .productName("Product 1")
                .categories(List.of(new CategoryResponseDTO(1, "Category 1", "Description 1")))
                .build();

        // Mock a Page object using PageImpl
        PageRequest pageRequest = PageRequest.of(0, 8);
        Page<ProductCategoryResponseDTO> page = new PageImpl<>(List.of(responseDTO), pageRequest, 1);

        // Mock service call
        when(productCategoryService.getAllProductCategories(anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(new APIPageableResponseDTO<>(page));

        // Call the controller method
        APIPageableResponseDTO<ProductCategoryResponseDTO> response = productCategoryController.getAllCategory(0, 8, "", "id");

        // Assertions
        assertEquals(1, response.getContent().size());
        assertEquals("Product 1", response.getContent().get(0).getProductName());
    }

    @Test
    void testGetProductCategoryById() throws Exception {
        ProductCategoryResponseDTO responseDTO = ProductCategoryResponseDTO.builder()
                .productId(1)
                .productName("Product 1")
                .categories(List.of(new CategoryResponseDTO(1, "Category 1", "Description 1")))
                .build();

        // Mock the service call
        when(productCategoryService.getProductCategoriesByProductId(1)).thenReturn(responseDTO);

        // Perform the request and verify the result
        MvcResult result = mockMvc.perform(get("/product-categories/{productId}", 1))
                .andExpect(status().isOk()) // Check that the status is OK
                .andExpect(jsonPath("$.productId").value(1)) // Check productId
                .andExpect(jsonPath("$.productName").value("Product 1")) // Check productName
                .andExpect(jsonPath("$.categories[0].name").value("Category 1")) // Check category name
                .andExpect(jsonPath("$.categories[0].description").value("Description 1")) // Check category description
                .andReturn();

        // Log the response content for debugging
        System.out.println("Response Content: " + result.getResponse().getContentAsString());

        // Verify service interaction
        verify(productCategoryService, times(1)).getProductCategoriesByProductId(1);
    }


}
