package com.example.apisample.controller;


import com.example.apisample.role.controller.RoleController;
import com.example.apisample.role.entity.Role;
import com.example.apisample.role.model.dto.AssignRoleRequestDTO;
import com.example.apisample.role.service.RoleService;
import com.example.apisample.utils.ApiResponse;
import com.example.apisample.utils.message.LogMessage;
import com.example.apisample.utils.message.ResponseMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RoleControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private RoleController roleController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(roleController).build();
    }

    @Test
    void testGetAllRoles() throws Exception {
        // Prepare mock response
        Role role = new Role(1, "Admin");
        when(roleService.getAllRole()).thenReturn(List.of(role));

        // Perform the GET request and assert the response
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/roles")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value(ResponseMessage.msgSuccess))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].roleName").value("Admin"));

        // Verify interactions with the service
        verify(roleService, times(1)).getAllRole();
    }

    @Test
    void testGetRoleById() throws Exception {
        // Prepare mock response
        Role role = new Role(1, "Admin");
        when(roleService.getRoleById(1)).thenReturn(role);

        // Perform the GET request and assert the response
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/roles/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value(ResponseMessage.msgSuccess))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.roleName").value("Admin"));

        // Verify interactions with the service
        verify(roleService, times(1)).getRoleById(1);
    }

    @Test
    void testAssignRole() throws Exception {
        // Prepare the DTO and mock service response
        AssignRoleRequestDTO dto = new AssignRoleRequestDTO("user@example.com", 1);
        doNothing().when(roleService).assignRole(dto.getEmail(), dto.getRoleId());

        // Perform the POST request and assert the response
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/users/{id}/roles", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"user@example.com\", \"roleId\": 1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value(ResponseMessage.msgSuccess));

        // Verify interactions with the service
        verify(roleService, times(1)).assignRole(dto.getEmail(), dto.getRoleId());
    }
}
