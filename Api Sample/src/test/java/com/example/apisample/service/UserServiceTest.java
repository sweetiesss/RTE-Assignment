package com.example.apisample.service;


import com.example.apisample.auth.service.EmailService;
import com.example.apisample.role.entity.Role;
import com.example.apisample.role.repository.RoleRepository;
import com.example.apisample.user.entity.User;
import com.example.apisample.user.exception.UserAlreadyExistsException;
import com.example.apisample.user.exception.UserDoesNotExistException;
import com.example.apisample.user.model.dto.UserRegisterRequestDTO;
import com.example.apisample.user.model.dto.UserResponseDTO;
import com.example.apisample.user.model.dto.UserUpdateRequestDTO;
import com.example.apisample.user.repository.UserRepository;
import com.example.apisample.user.service.impl.UserServiceImpl;
import com.example.apisample.utils.pagination.APIPageableResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private Role role;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize a mock user and role for testing
        role = new Role();
        role.setId(1);
        role.setRoleName("USER");

        user = new User();
        user.setId(1);
        user.setEmail("test@example.com");
        user.setRole(role);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPhone("1234567890");
    }

    @Test
    void getALlUser_ShouldReturnPagedListOfUsers() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10, Sort.by("email").ascending());  // Adjust the sort if needed

        // Create User entity with mock data
        User user = new User();
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPhone("1234567890");
        user.setDeleted(false);
        user.setRole(new Role(1, "USER"));

        // Create a list of users
        List<User> users = new ArrayList<>();
        users.add(user);

        // Create a PageImpl with the list of User objects
        Page<User> userPage = new PageImpl<>(users, pageable, users.size());

        // Mock the repository method to return the page
        when(userRepository.findByEmailContainingAndRole_Id("test", 2, pageable)).thenReturn(userPage);

        // Act
        APIPageableResponseDTO<UserResponseDTO> result = userService.getALlUser(0, 10, "test", "email");

        // Assert
        assertNotNull(result, "Result should not be null");
        assertNotNull(result.getContent(), "Content should not be null");
        assertEquals(1, result.getContent().size(), "Should return 1 user");

        // Verify that the content of the response matches expected values
        UserResponseDTO userResponseDTO = result.getContent().get(0);
        assertEquals("test@example.com", userResponseDTO.getEmail(), "User email should be 'test@example.com'");
        assertEquals("John", userResponseDTO.getFirstName(), "User first name should be 'John'");
        assertEquals("Doe", userResponseDTO.getLastName(), "User last name should be 'Doe'");
    }


    @Test
    void getUserById_ShouldReturnUser() {
        // Arrange
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        // Act
        UserResponseDTO result = userService.getUserById(1);

        // Assert
        assertNotNull(result);
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void getUserById_ShouldThrowUserDoesNotExistException() {
        // Arrange
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserDoesNotExistException.class, () -> userService.getUserById(1));
    }

    @Test
    void getUserByEmail_ShouldReturnUser() {
        // Arrange
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);

        // Act
        User result = userService.getUserByEmail("test@example.com");

        // Assert
        assertNotNull(result);
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void getUserByEmail_ShouldThrowUserDoesNotExistException() {
        // Arrange
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(null);

        // Act & Assert
        assertThrows(UserDoesNotExistException.class, () -> userService.getUserByEmail("nonexistent@example.com"));
    }

    @Test
    void register_ShouldRegisterUserSuccessfully() {
        // Arrange
        UserRegisterRequestDTO registerUserDTO = new UserRegisterRequestDTO();
        registerUserDTO.setEmail("newuser@example.com");
        registerUserDTO.setFirstName("New");
        registerUserDTO.setLastName("User");
        registerUserDTO.setPhone("0987654321");

        when(userRepository.findByEmail(registerUserDTO.getEmail())).thenReturn(null);
        when(roleRepository.findById(2)).thenReturn(Optional.of(role));

        // Act
        userService.register(registerUserDTO);

        // Assert
        verify(userRepository, times(1)).save(any(User.class));  // Verify save method is called
        verify(emailService, times(1)).sendPasswordEmail(eq(registerUserDTO.getEmail()), anyString());  // Verify email is sent
    }

    @Test
    void register_ShouldThrowUserAlreadyExistsException() {
        // Arrange
        UserRegisterRequestDTO registerUserDTO = new UserRegisterRequestDTO();
        registerUserDTO.setEmail("existinguser@example.com");

        when(userRepository.findByEmail(registerUserDTO.getEmail())).thenReturn(user);  // Mock existing user

        // Act & Assert
        assertThrows(UserAlreadyExistsException.class, () -> userService.register(registerUserDTO));
    }

    @Test
    void update_ShouldUpdateUserSuccessfully() {
        // Arrange
        UserUpdateRequestDTO updateDTO = new UserUpdateRequestDTO();
        updateDTO.setEmail("updated@example.com");
        updateDTO.setFirstName("Updated");
        updateDTO.setLastName("User");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        // Act
        userService.update(1, updateDTO);

        // Assert
        assertEquals("updated@example.com", user.getEmail());
        assertEquals("Updated", user.getFirstName());
        assertEquals("User", user.getLastName());
        verify(userRepository, times(1)).save(user);  // Verify save method is called
    }

    @Test
    void deleteUser_ShouldMarkUserAsDeleted() {
        // Arrange
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        // Act
        userService.deleteUser(1);

        // Assert
        assertTrue(user.getDeleted());
        verify(userRepository, times(1)).save(user);  // Verify save method is called
    }

    @Test
    void restoreUser_ShouldRestoreUser() {
        // Arrange
        user.setDeleted(true);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        // Act
        userService.restoreUser(1);

        // Assert
        assertFalse(user.getDeleted());
        verify(userRepository, times(1)).save(user);  // Verify save method is called
    }
}
