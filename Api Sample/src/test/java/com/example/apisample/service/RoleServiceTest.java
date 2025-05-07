package com.example.apisample.service;


import com.example.apisample.role.entity.Role;
import com.example.apisample.role.exception.RoleDoesNotExistException;
import com.example.apisample.role.repository.RoleRepository;
import com.example.apisample.role.service.impl.RoleServiceImpl;
import com.example.apisample.user.entity.User;
import com.example.apisample.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private RoleServiceImpl roleService;

    private Role role;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize a mock role and user for testing
        role = new Role();
        role.setId(1);
        role.setRoleName("ADMIN");

        user = new User();
        user.setEmail("test@example.com");
        user.setRole(role);
    }

    @Test
    void getAllRole_ShouldReturnListOfRoles() {
        // Arrange
        List<Role> roles = Arrays.asList(role);
        when(roleRepository.findAll()).thenReturn(roles);

        // Act
        List<Role> result = roleService.getAllRole();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(role, result.get(0));
    }

    @Test
    void getRoleById_ShouldReturnRole() {
        // Arrange
        when(roleRepository.findById(1)).thenReturn(Optional.of(role));

        // Act
        Role result = roleService.getRoleById(1);

        // Assert
        assertNotNull(result);
        assertEquals(role, result);
    }

    @Test
    void getRoleById_ShouldThrowRoleDoesNotExistException() {
        // Arrange
        when(roleRepository.findById(2)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RoleDoesNotExistException.class, () -> roleService.getRoleById(2));
    }

    @Test
    void assignRole_ShouldAssignRoleToUser() {
        // Arrange
        String email = "test@example.com";
        Integer roleId = 1;
        when(userService.getUserByEmail(email)).thenReturn(user);
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));

        // Act
        roleService.assignRole(email, roleId);

        // Assert
        assertEquals(role, user.getRole());  // Verify that the user's role is updated
        verify(userService, times(1)).saveUser(user);  // Verify that saveUser was called
    }

    @Test
    void assignRole_ShouldThrowRoleDoesNotExistException_WhenRoleNotFound() {
        // Arrange
        String email = "test@example.com";
        Integer roleId = 2; // Non-existing role ID
        when(userService.getUserByEmail(email)).thenReturn(user);
        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RoleDoesNotExistException.class, () -> roleService.assignRole(email, roleId));
    }
}
