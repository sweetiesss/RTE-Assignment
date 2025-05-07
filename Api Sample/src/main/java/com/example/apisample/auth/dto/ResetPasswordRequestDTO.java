package com.example.apisample.auth.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class ResetPasswordRequestDTO {
    private String otp;
    @Email(message = "Invalid email format")
    private String email;
    private String password;
}
