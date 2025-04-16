package com.example.apisample.model.dto.authdto;

import lombok.Data;

@Data
public class ResetPasswordRequestDTO {
    private String otp;
    private String email;
    private String password;
}
