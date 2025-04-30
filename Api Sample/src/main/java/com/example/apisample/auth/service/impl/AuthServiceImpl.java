package com.example.apisample.auth.service.impl;

import com.example.apisample.auth.dto.ResetPasswordRequestDTO;
import com.example.apisample.auth.enums.OtpType;
import com.example.apisample.auth.exception.InvalidCredentialsException;
import com.example.apisample.auth.service.AuthService;
import com.example.apisample.auth.service.OtpService;
import com.example.apisample.user.entity.User;
import com.example.apisample.user.exception.UserDeletedException;
import com.example.apisample.user.exception.UserDoesNotExistException;
import com.example.apisample.user.exception.UserDoesNotLoginException;
import com.example.apisample.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final OtpService otpService;

    private static final String REFRESH_TOKEN_COOKIE_NAME = "refresh-token";

    @Override
    public void login(String email, String password) {

        User user = userService.getUserByEmail(email);

        if(user == null){
            throw new UserDoesNotExistException();
        }

        if(user.getDeleted()){
            throw new UserDeletedException();
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        otpService.generateOtp(user, OtpType.LOGIN_2FA);
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequestDTO dto, User user) {
        otpService.validateOtp(user, dto.getOtp(), OtpType.PASSWORD_RESET);

        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setLastUpdateOn(Instant.now());
        user.setTokenVersion(user.getTokenVersion() + 1);

        userService.saveUser(user);
    }

    @Override
    @Transactional
    public void logout(HttpServletResponse response, User user) {
        if(user == null){
            throw new UserDoesNotLoginException();
        }

        user.setTokenVersion(user.getTokenVersion() + 1);
        userService.saveUser(user);

        Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
    }
}
