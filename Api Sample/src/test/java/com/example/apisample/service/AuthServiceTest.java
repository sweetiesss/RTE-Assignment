package com.example.apisample.service;

import com.example.apisample.auth.dto.ResetPasswordRequestDTO;
import com.example.apisample.auth.enums.OtpType;
import com.example.apisample.auth.exception.InvalidCredentialsException;
import com.example.apisample.auth.service.OtpService;
import com.example.apisample.auth.service.impl.AuthServiceImpl;
import com.example.apisample.user.entity.User;
import com.example.apisample.user.exception.UserDeletedException;
import com.example.apisample.user.exception.UserDoesNotExistException;
import com.example.apisample.user.exception.UserDoesNotLoginException;
import com.example.apisample.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private OtpService otpService;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private AuthServiceImpl authService;

    private User user;
    private ResetPasswordRequestDTO resetPasswordRequestDTO;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setDeleted(false);
        user.setTokenVersion(1);
        user.setLastUpdateOn(Instant.now());

        resetPasswordRequestDTO = new ResetPasswordRequestDTO();
        resetPasswordRequestDTO.setOtp("123456");
        resetPasswordRequestDTO.setPassword("newPassword");
    }

    @Test
    void testLogin_UserNotFound() {
        when(userService.getUserByEmail(user.getEmail())).thenReturn(null);

        assertThrows(UserDoesNotExistException.class, () -> authService.login(user.getEmail(), user.getPassword()));
    }

    @Test
    void testLogin_UserDeleted() {
        user.setDeleted(true);
        when(userService.getUserByEmail(user.getEmail())).thenReturn(user);

        assertThrows(UserDeletedException.class, () -> authService.login(user.getEmail(), user.getPassword()));
    }

    @Test
    void testLogin_InvalidCredentials() {
        when(userService.getUserByEmail(user.getEmail())).thenReturn(user);
        when(passwordEncoder.matches(user.getPassword(), "wrongPassword")).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> authService.login(user.getEmail(), "wrongPassword"));
    }

    @Test
    void testLogin_Success() {
        when(userService.getUserByEmail(user.getEmail())).thenReturn(user);
        when(passwordEncoder.matches(user.getPassword(), "password")).thenReturn(true);

        authService.login(user.getEmail(), user.getPassword());

        verify(otpService, times(1)).generateOtp(user, OtpType.LOGIN_2FA);
    }

    @Test
    void testResetPassword_Success() {
        doNothing().when(otpService).validateOtp(user, resetPasswordRequestDTO.getOtp(), OtpType.PASSWORD_RESET);

        doNothing().when(userService).saveUser(user);

        authService.resetPassword(resetPasswordRequestDTO, user);

        assertEquals(user.getPassword(), passwordEncoder.encode(resetPasswordRequestDTO.getPassword()));
        verify(userService, times(1)).saveUser(user);
    }

    @Test
    void testResetPassword_InvalidOtp() {
        doThrow(InvalidCredentialsException.class).when(otpService)
                .validateOtp(user, resetPasswordRequestDTO.getOtp(), OtpType.PASSWORD_RESET);
        assertThrows(InvalidCredentialsException.class, () -> authService.resetPassword(resetPasswordRequestDTO, user));
    }

    @Test
    void testLogout_UserNotLoggedIn() {
        assertThrows(UserDoesNotLoginException.class, () -> authService.logout(response, null));
    }

    @Test
    void testLogout_Success() {
        when(userService.getUserByEmail(user.getEmail())).thenReturn(user);

        authService.logout(response, user);

        assertEquals(user.getTokenVersion(), 2);
        verify(userService, times(1)).saveUser(user);

        // Verify cookie creation
        Cookie cookie = new Cookie("refresh-token", null);
        assertNotNull(cookie);
        assertEquals(-1, cookie.getMaxAge());
    }
}
