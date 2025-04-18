package com.example.apisample.model.dto.otp;

import lombok.Data;

@Data
public class LoginOtpRequestDTO {
    private String otp;
    private String email;
}
