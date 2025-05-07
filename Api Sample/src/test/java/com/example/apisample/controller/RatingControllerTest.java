package com.example.apisample.controller;


import com.example.apisample.rating.controller.RatingController;
import com.example.apisample.rating.model.dto.RatingRequestDTO;
import com.example.apisample.rating.model.dto.RatingResponseDTO;
import com.example.apisample.rating.model.dto.RatingUpdateRequestDTO;
import com.example.apisample.rating.service.RatingService;
import com.example.apisample.utils.message.ResponseMessage;
import com.example.apisample.utils.pagination.APIPageableResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.List;

import static org.mockito.Mockito.*;

class RatingControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RatingService ratingService;

    @InjectMocks
    private RatingController ratingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(ratingController).build();
    }

    @Test
    void testCreateRating() throws Exception {
        RatingRequestDTO ratingRequestDTO = RatingRequestDTO.builder()
                .userId(1)
                .productId(1)
                .point(5)
                .description("Great product!")
                .build();

        // Perform the request and verify the result
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/ratings")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(ratingRequestDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(HttpStatus.CREATED.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ResponseMessage.msgSuccess))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").value(ResponseMessage.msgSuccess))
                .andReturn();

        // Log the response content for debugging
        System.out.println("Response Content: " + result.getResponse().getContentAsString());

        // Verify service interaction
        verify(ratingService, times(1)).createRating(any(RatingRequestDTO.class));
    }

    @Test
    void testGetRatingById() throws Exception {
        RatingResponseDTO ratingResponseDTO = RatingResponseDTO.builder()
                .id(1)
                .userEmail("user@example.com")
                .productName("Product A")
                .point(5)
                .description("Great product!")
                .deleted("No")
                .createOn(Instant.now())
                .lastUpdateOn(Instant.now())
                .build();

        // Mock the service call
        when(ratingService.getRatingById(1)).thenReturn(ratingResponseDTO);

        // Perform the request and verify the result
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/ratings/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ResponseMessage.msgSuccess))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.userEmail").value("user@example.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.productName").value("Product A"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.point").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.description").value("Great product!"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.deleted").value("No"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.createOn").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.lastUpdateOn").exists())
                .andReturn();

        // Log the response content for debugging
        System.out.println("Response Content: " + result.getResponse().getContentAsString());

        // Verify service interaction
        verify(ratingService, times(1)).getRatingById(1);
    }

    @Test
    void testUpdateRating() throws Exception {
        RatingUpdateRequestDTO ratingUpdateRequestDTO = RatingUpdateRequestDTO.builder()
                .point(4)
                .description("Updated description")
                .build();

        // Perform the request and verify the result
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/ratings/{id}", 1)
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(ratingUpdateRequestDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ResponseMessage.msgSuccess))
                .andReturn();

        // Log the response content for debugging
        System.out.println("Response Content: " + result.getResponse().getContentAsString());

        // Verify service interaction
        verify(ratingService, times(1)).updateRating(eq(1), any(RatingUpdateRequestDTO.class));
    }

    @Test
    void testDeleteRating() throws Exception {
        // Perform the request and verify the result
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/ratings/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ResponseMessage.msgSuccess))
                .andReturn();

        // Log the response content for debugging
        System.out.println("Response Content: " + result.getResponse().getContentAsString());

        // Verify service interaction
        verify(ratingService, times(1)).deleteRating(1);
    }

    @Test
    void testRestoreRating() throws Exception {
        // Perform the request and verify the result
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/admin/ratings/restore/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ResponseMessage.msgSuccess))
                .andReturn();

        // Log the response content for debugging
        System.out.println("Response Content: " + result.getResponse().getContentAsString());

        // Verify service interaction
        verify(ratingService, times(1)).restoreRating(1);
    }

    @Test
    void testGetProductRatings() throws Exception {
        // Create a mock response
        RatingResponseDTO ratingResponseDTO = RatingResponseDTO.builder()
                .id(1)
                .userEmail("user@example.com")
                .productName("Product A")
                .point(5)
                .description("Great product!")
                .deleted("No")
                .createOn(Instant.now())
                .lastUpdateOn(Instant.now())
                .build();

        APIPageableResponseDTO<RatingResponseDTO> pageableResponseDTO = new APIPageableResponseDTO<>();
        pageableResponseDTO.setContent(List.of(ratingResponseDTO));  // Add a mock rating

        // Mock service call to return the mock pageable response
        when(ratingService.getRatingsByProductId(1, 0, 8, "createOn")).thenReturn(pageableResponseDTO);

        // Perform the request and verify the result
        mockMvc.perform(MockMvcRequestBuilders.get("/products/{productId}/ratings", 1)
                        .param("page", "0")
                        .param("size", "8")
                        .param("sort", "createOn"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray()) // Check if content is an array
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].userEmail").value("user@example.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].productName").value("Product A"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].point").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].description").value("Great product!"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].deleted").value("No"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].createOn").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].lastUpdateOn").exists())
                .andReturn();
    }
}

