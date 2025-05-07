package com.example.apisample.auth.service.impl;

import com.example.apisample.auth.exception.TokenExpiredException;
import com.example.apisample.auth.service.JWTService;
import com.example.apisample.user.entity.User;
import com.example.apisample.user.service.UserService;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JWTServiceImpl implements JWTService {
    private final UserService userService;

    @Value("${jwt.access-token-expiration-ms}")
    private long accessTokenExpirationMs;

    @Value("${jwt.refresh-token-expiration-ms}")
    private long refreshTokenExpirationMs;

    private static final Dotenv dotenv = Dotenv.load();
    private static final String SECRET_KEY = dotenv.get("JWT_SECRET");
    private static final String REFRESH_TOKEN_COOKIE_NAME = "refresh-token";
    private static final String CLAIM_NAME_TOKEN_VERSION = "token-version";
    private static final String CLAIM_NAME_USER_ID = "userId";
    private static final String CLAIM_NAME_ROLE = "role";
    private static final String CLAIM_NAME_JTI = "jti";
    private static final String JWT_TYPE_NAME = "typ";
    private static final String JWT_TYPE_VALUE = "JWT";

    @Override
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
    }

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);

        return claimsResolver.apply(claims);
    }

    @Override
    public String generateToken(Map<String, Object> extraClaims, User user) {
        return Jwts.builder()
                .setHeaderParam(JWT_TYPE_NAME, JWT_TYPE_VALUE)
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .claim(CLAIM_NAME_ROLE, user.getRole().getRoleName())
                .claim(CLAIM_NAME_JTI, UUID.randomUUID().toString())
                .claim(CLAIM_NAME_TOKEN_VERSION, user.getTokenVersion())
                .claim(CLAIM_NAME_USER_ID, user.getId())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpirationMs))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String generateRefreshToken(HttpServletResponse response, User user) {
        String refreshToken = Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .claim(CLAIM_NAME_TOKEN_VERSION, user.getTokenVersion())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpirationMs))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

        Cookie refreshTokenCookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge((int) (refreshTokenExpirationMs / 1000));

        response.addCookie(refreshTokenCookie);

        return refreshToken;
    }

    @Override
    public String generateAccessToken(User user) {
        return generateToken(new HashMap<>(), user);
    }

    public Key getSignInKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
    }

    @Override
    public boolean isValidToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        Claims claims = extractAllClaims(token);

        if (!username.equals(userDetails.getUsername())) return false;

        // Validate token version
        Integer tokenVersion = claims.get(CLAIM_NAME_TOKEN_VERSION, Integer.class);
        User user = (User) userDetails;
        return tokenVersion != null && tokenVersion.equals(user.getTokenVersion()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    @Override
    public String generateAccessTokenFromCookie(HttpServletRequest request) throws TokenExpiredException {
        String refreshToken = Optional.ofNullable(request.getCookies())
                .flatMap(cookies -> Arrays.stream(cookies)
                        .filter(cookie -> cookie.getName().equals(REFRESH_TOKEN_COOKIE_NAME))
                        .findFirst()
                        .map(Cookie::getValue))
                .orElse(null);

        String email = extractUsername(refreshToken);

        User user = userService.getUserByEmail(email);

        if (isValidToken(refreshToken, user)) {
            return generateAccessToken(user);
        } else {
            throw new TokenExpiredException();
        }
    }
}
