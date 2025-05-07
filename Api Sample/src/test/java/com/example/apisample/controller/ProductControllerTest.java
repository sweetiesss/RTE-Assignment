package com.example.apisample.controller;

import com.example.apisample.product.controller.ProductController;
import com.example.apisample.product.model.dto.ProductCreateDTO;
import com.example.apisample.product.model.dto.ProductResponseDTO;
import com.example.apisample.product.model.dto.ProductUpdateDTO;
import com.example.apisample.product.service.ProductService;
import com.example.apisample.utils.pagination.APIPageableResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetProductById() throws Exception {
        ProductResponseDTO mockProduct = ProductResponseDTO.builder()
                .id(1)
                .name("Test Product")
                .description("Test Description")
                .price(100L)
                .image("image.jpg")
                .featured(true)
                .deleted("false")
                .createOn(Instant.now())
                .lastUpdateOn(Instant.now())
                .averageRating(4.5)
                .build();

        when(productService.getProductById(1)).thenReturn(mockProduct);

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Test Product"))
                .andExpect(jsonPath("$.data.price").value(100));
    }

    @Test
    void testCreateProduct() throws Exception {
        ProductCreateDTO dto = new ProductCreateDTO();
        dto.setName("New Product");
        dto.setDescription("New Description");
        dto.setPrice(200L);
        dto.setFeatured(true);

        mockMvc.perform(post("/products/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statusCode").value(201));

        verify(productService, times(1)).createProduct(any(ProductCreateDTO.class));
    }

    @Test
    void testUpdateProduct() throws Exception {
        ProductUpdateDTO updateDTO = ProductUpdateDTO.builder()
                .name("Updated Name")
                .price(300L)
                .featured(true)
                .deleted(false)
                .build();

        mockMvc.perform(put("/admin/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200));

        verify(productService, times(1)).updateProduct(eq(1), any(ProductUpdateDTO.class));
    }

    @Test
    void testDeleteProduct() throws Exception {
        mockMvc.perform(delete("/admin/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200));

        verify(productService, times(1)).deleteProduct(1);
    }

    @Test
    void testRestoreProduct() throws Exception {
        mockMvc.perform(patch("/admin/products/1/restore"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200));

        verify(productService, times(1)).restoreProduct(1);
    }

    @Test
    void testGetAllProduct() throws Exception {
        APIPageableResponseDTO<ProductResponseDTO> pageResponse = new APIPageableResponseDTO<>();
        pageResponse.setContent(List.of());
        pageResponse.setPageable(null);

        when(productService.getAllProduct(anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(pageResponse);

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk());

        verify(productService, times(1)).getAllProduct(anyInt(), anyInt(), anyString(), anyString());
    }
}
