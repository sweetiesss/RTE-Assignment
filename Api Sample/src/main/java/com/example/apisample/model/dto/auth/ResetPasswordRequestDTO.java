package com.example.apisample.model.dto.auth;

import lombok.Data;

@Data
public class ResetPasswordRequestDTO {
    private String otp;
    private String email;
    private String password;
}
