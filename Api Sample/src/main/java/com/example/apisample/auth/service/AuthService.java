package com.example.apisample.auth.service;

import com.example.apisample.auth.dto.ResetPasswordRequestDTO;
import com.example.apisample.user.entity.User;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    void login(String email, String password);
    void resetPassword(ResetPasswordRequestDTO dto, User user);
    void logout(HttpServletResponse response, User user);
}
