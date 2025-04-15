package com.example.apisample.model.dto.otpdto;

import com.example.apisample.enums.OtpType;
import lombok.Data;

@Data
public class OtpRequestDTO {
    private String otp;
    private String email;
    private OtpType type;
}
