package com.example.apisample.controller;

import com.example.apisample.auth.controller.AuthController;
import com.example.apisample.auth.dto.LoginOtpRequestDTO;
import com.example.apisample.auth.dto.LoginRequestDTO;
import com.example.apisample.auth.dto.ResetPasswordRequestDTO;
import com.example.apisample.auth.enums.OtpType;
import com.example.apisample.auth.service.AuthService;
import com.example.apisample.auth.service.JWTService;
import com.example.apisample.auth.service.OtpService;
import com.example.apisample.user.entity.User;
import com.example.apisample.user.model.dto.UserRegisterRequestDTO;
import com.example.apisample.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @Mock
    private JWTService jwtService;

    @Mock
    private OtpService otpService;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testSignIn() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("user@example.com");
        request.setPassword("password");

        doNothing().when(authService).login(anyString(), anyString());

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("OTP sent to your email!"));
    }

    @Test
    void testVerifyOtp() throws Exception {
        LoginOtpRequestDTO request = new LoginOtpRequestDTO();
        request.setEmail("user@example.com");
        request.setOtp("123456");

        User user = new User();
        String accessToken = "access-token";
        String refreshToken = "refresh-token";

        when(userService.getUserByEmail(anyString())).thenReturn(user);
        doNothing().when(otpService).validateOtp(any(), any(), eq(OtpType.LOGIN_2FA));
        when(jwtService.generateRefreshToken(any(HttpServletResponse.class), eq(user))).thenReturn(refreshToken);
        when(jwtService.generateAccessToken(user)).thenReturn(accessToken);

        mockMvc.perform(post("/auth/login-verify-otp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").value(accessToken));
    }

    @Test
    void testRefreshToken() throws Exception {
        String newToken = "new-access-token";
        when(jwtService.generateAccessTokenFromCookie(any(HttpServletRequest.class))).thenReturn(newToken);

        mockMvc.perform(post("/auth/refresh-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(newToken));
    }

    @Test
    void testResetPasswordRequest_withOtp() throws Exception {
        ResetPasswordRequestDTO dto = new ResetPasswordRequestDTO();
        dto.setEmail("user@example.com");
        dto.setPassword("newPass");
        dto.setOtp("123456");
        User user = new User();

        when(userService.getUserByEmail(anyString())).thenReturn(user);
        doNothing().when(authService).resetPassword(dto, user);

        mockMvc.perform(post("/auth/reset-password-request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Password has been update successfully!"));
    }

    @Test
    void testResetPasswordRequest_withoutOtp() throws Exception {
        ResetPasswordRequestDTO dto = new ResetPasswordRequestDTO();
        dto.setEmail("user@example.com");
        dto.setOtp(null);
        dto.setPassword("newPass");
        User user = new User();

        when(userService.getUserByEmail(anyString())).thenReturn(user);
        doNothing().when(otpService).generateOtp(user, OtpType.PASSWORD_RESET);

        mockMvc.perform(post("/auth/reset-password-request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("OTP sent to your email!"));
    }

    @Test
    void testRegister() throws Exception {
        UserRegisterRequestDTO dto = new UserRegisterRequestDTO();
        dto.setEmail("user@example.com");
        dto.setLastName("LastName");
        dto.setFirstName("FirstName");
        dto.setPhone("0398764758");

        doNothing().when(userService).register(dto);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("successfully"));
    }
}
