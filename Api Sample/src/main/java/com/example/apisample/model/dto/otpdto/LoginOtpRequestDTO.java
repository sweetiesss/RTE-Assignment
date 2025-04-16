package com.example.apisample.model.dto.otpdto;

import com.example.apisample.enums.OtpType;
import lombok.Data;

@Data
public class LoginOtpRequestDTO {
    private String otp;
    private String email;
}
