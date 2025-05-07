package com.example.apisample.controller;


import com.example.apisample.user.controller.UserController;
import com.example.apisample.user.model.dto.UserResponseDTO;
import com.example.apisample.user.model.dto.UserUpdateRequestDTO;
import com.example.apisample.user.service.UserService;
import com.example.apisample.utils.ApiResponse;
import com.example.apisample.utils.message.ResponseMessage;
import com.example.apisample.utils.pagination.APIPageableResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void testGetAllUsers() throws Exception {
        UserResponseDTO user = new UserResponseDTO(1, "user@example.com", null, "John", "Doe", "1234567890", null, null, "USER");

        Pageable pageable = PageRequest.of(0, 8);

        List<UserResponseDTO> userList = List.of(user);
        Page<UserResponseDTO> page = new PageImpl<>(userList, pageable, userList.size());

        when(userService.getALlUser(0, 8, "", "id")).thenReturn(new APIPageableResponseDTO<>(page));

        // Perform the GET request and assert the response
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "8")
                        .param("search", "")
                        .param("sort", "id"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].email").value("user@example.com"));

        verify(userService, times(1)).getALlUser(0, 8, "", "id");
    }

    @Test
    void testGetUserById() throws Exception {
        // Prepare mock response
        UserResponseDTO user = new UserResponseDTO(1, "user@example.com", null, "John", "Doe", "1234567890", null, null, "USER");
        when(userService.getUserById(1)).thenReturn(user);

        // Perform the GET request and assert the response
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value(ResponseMessage.msgSuccess))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.email").value("user@example.com"));

        // Verify interactions with the service
        verify(userService, times(1)).getUserById(1);
    }

    @Test
    void testUpdateUser() throws Exception {
        // Prepare the update DTO
        UserUpdateRequestDTO updateDTO = new UserUpdateRequestDTO("newemail@example.com", "Jane", "Doe", "0987654321", null, null);
        doNothing().when(userService).update(1, updateDTO);

        // Perform the PUT request and assert the response
        mockMvc.perform(MockMvcRequestBuilders.put("/admin/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"newemail@example.com\", \"firstName\": \"Jane\", \"lastName\": \"Doe\", \"phone\": \"0987654321\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value(ResponseMessage.msgSuccess));

        // Verify interactions with the service
        verify(userService, times(1)).update(1, updateDTO);
    }

    @Test
    void testDeleteUser() throws Exception {
        // Prepare mock response
        doNothing().when(userService).deleteUser(1);

        // Perform the DELETE request and assert the response
        mockMvc.perform(MockMvcRequestBuilders.delete("/admin/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value(ResponseMessage.msgSuccess));

        // Verify interactions with the service
        verify(userService, times(1)).deleteUser(1);
    }

    @Test
    void testRestoreUser() throws Exception {
        // Prepare mock response
        doNothing().when(userService).restoreUser(1);

        // Perform the PATCH request and assert the response
        mockMvc.perform(MockMvcRequestBuilders.patch("/admin/users/{id}/restore", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value(ResponseMessage.msgSuccess));

        // Verify interactions with the service
        verify(userService, times(1)).restoreUser(1);
    }
}

