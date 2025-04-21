package com.example.apisample.model.dto.otp;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class LoginOtpRequestDTO {

    @NotBlank(message = "OTP is required")
    @Pattern(regexp = "\\d{6}", message = "OTP must be exactly 6 digits")
    private String otp;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
}
