package com.example.apisample.service.Implement;

import com.example.apisample.entity.User;
import com.example.apisample.exception.jwtservice.TokenExpiredException;
import com.example.apisample.exception.userservice.InvalidateException;
import com.example.apisample.exception.userservice.UserDoesNotExistException;
import com.example.apisample.service.Interface.JWTService;
import com.example.apisample.service.Interface.UserService;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JWTServiceImp implements JWTService {
    private final UserService userService;
    private static final Dotenv dotenv = Dotenv.load();
    private static final String SECRET_KEY = dotenv.get("JWT_SECRET");
    private static final String CLAIM_NAME_TOKEN_VERSION = "token-version";
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
    public String generateToken(Map<String, Object> extraClaims, User user) { // generate token with claims (access token)
        return Jwts.builder()
                .setHeaderParam(JWT_TYPE_NAME, JWT_TYPE_VALUE)
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .claim(CLAIM_NAME_ROLE, user.getRole().getRoleName())
                .claim(CLAIM_NAME_JTI, UUID.randomUUID().toString())
                .claim(CLAIM_NAME_TOKEN_VERSION, user.getTokenVersion())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 1 day access token, for dev only
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String generateRefreshToken(User user) {
        return Jwts.builder().setClaims(new HashMap<>()).setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .claim(CLAIM_NAME_TOKEN_VERSION, user.getTokenVersion())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)) // 7 days refresh token
                .signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
    }

    @Override
    public String generateAccessToken(User user) {
        return generateToken(new HashMap<>(), user);
    }

    private Key getSignInKey() {
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
    public String generateAccessToken(String refreshToken) throws TokenExpiredException, UserDoesNotExistException, InvalidateException {
        String email = extractUsername(refreshToken);
        User user = userService.getUserByEmail(email);

        if (isValidToken(refreshToken, user)) {
            return generateAccessToken(user);
        } else {
            throw new TokenExpiredException();
        }
    }
}
