package com.example.apisample.service.Interface;

import com.example.apisample.entity.User;
import com.example.apisample.exception.jwtservice.TokenExpiredException;
import com.example.apisample.exception.userservice.InvalidateException;
import com.example.apisample.exception.userservice.UserDoesNotExistException;
import io.jsonwebtoken.Claims;
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
    String generateRefreshToken(User user);

//    void detachToken(String token) throws UserDoesNotExistException, InvalidateException;
    boolean isValidToken(String token, UserDetails user);
    String generateAccessToken(String refreshToken) throws TokenExpiredException, UserDoesNotExistException, InvalidateException;
}
