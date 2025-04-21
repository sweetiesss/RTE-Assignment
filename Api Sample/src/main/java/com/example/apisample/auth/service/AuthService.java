package com.example.apisample.auth.service;

import com.example.apisample.auth.dto.ResetPasswordRequestDTO;
import com.example.apisample.auth.exception.*;
import com.example.apisample.user.entity.User;
import com.example.apisample.user.exception.UserDeletedException;
import com.example.apisample.user.exception.UserDoesNotExistException;
import com.example.apisample.user.exception.UserDoesNotLoginException;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    void login(String email, String password);
    void resetPassword(ResetPasswordRequestDTO dto, User user);
    void logout(HttpServletResponse response, User user);
}
