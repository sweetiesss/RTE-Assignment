package com.example.apisample.service;

import com.example.apisample.auth.exception.TokenExpiredException;
import com.example.apisample.auth.service.impl.JWTServiceImpl;
import com.example.apisample.role.entity.Role;
import com.example.apisample.user.entity.User;
import com.example.apisample.user.service.UserService;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.security.Key;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    @Spy
    private JWTServiceImpl jwtService;

    @Mock
    private UserService userService;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpServletRequest request;

    private User user;

    @BeforeEach
    void setUp() throws Exception{
        user = new User();
        user.setId(1);
        user.setEmail("test@example.com");
        user.setFirstName("testuser");
        user.setTokenVersion(0);
        user.setRole(new Role(1, "USER")); // Adjust constructor to match your Role class

        // Stub the getSignInKey() to use a dummy secret
        byte[] fakeKey = "12345678901234567890123456789012".getBytes(); // 32 bytes
        Key key = Keys.hmacShaKeyFor(fakeKey);
        doReturn(key).when(jwtService).getSignInKey();

        Field accessField = JWTServiceImpl.class.getDeclaredField("accessTokenExpirationMs");
        accessField.setAccessible(true);
        accessField.set(jwtService, 86400000L); // 1 day

        Field refreshField = JWTServiceImpl.class.getDeclaredField("refreshTokenExpirationMs");
        refreshField.setAccessible(true);
        refreshField.set(jwtService, 86400000L); // 1 day
    }

    @Test
    void testGenerateAccessTokenAndExtractUsername() {
        String token = jwtService.generateAccessToken(user);
        assertNotNull(token);

        String username = jwtService.extractUsername(token);
        assertEquals(user.getUsername(), username);
    }

    @Test
    void testGenerateRefreshTokenAddsCookie() {
        String token = jwtService.generateRefreshToken(response, user);
        assertNotNull(token);
        verify(response).addCookie(any(Cookie.class));
    }

    @Test
    void testIsValidToken() {
        String token = jwtService.generateAccessToken(user);
        boolean isValid = jwtService.isValidToken(token, user);
        assertTrue(isValid);
    }


    @Test
    void testGenerateAccessTokenFromCookie_valid() throws TokenExpiredException {
        String refreshToken = jwtService.generateRefreshToken(response, user);

        Cookie[] cookies = {new Cookie("refresh-token", refreshToken)};
        when(request.getCookies()).thenReturn(cookies);
        when(userService.getUserByEmail(user.getEmail())).thenReturn(user);

        String newToken = jwtService.generateAccessTokenFromCookie(request);
        assertNotNull(newToken);
    }

    @Test
    void testGenerateAccessTokenFromCookie_invalid_shouldThrow() {
        Cookie[] cookies = {new Cookie("refresh-token", "invalid.token")};
        when(request.getCookies()).thenReturn(cookies);

        assertThrows(Exception.class, () -> jwtService.generateAccessTokenFromCookie(request));
    }
}
