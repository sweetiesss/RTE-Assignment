package com.example.apisample.auth.service;

import com.example.apisample.auth.exception.TokenExpiredException;
import com.example.apisample.user.entity.User;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.function.Function;

public interface JWTService {
    Claims extractAllClaims(String token);
    String extractUsername(String token);
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
    String generateToken(
            Map<String, Object> extraClaims,
            User user
    );
    String generateAccessToken(User user);
    String generateRefreshToken(HttpServletResponse response, User user);

//    void detachToken(String token) throws UserDoesNotExistException, InvalidateException;
    boolean isValidToken(String token, UserDetails user);
    String generateAccessTokenFromCookie(HttpServletRequest request) throws TokenExpiredException;
}
