package com.example.apisample.auth.service;

import com.example.apisample.auth.enums.OtpType;
import com.example.apisample.user.entity.User;

public interface OtpService {
    void generateOtp(User user, OtpType type);
    void validateOtp(User user, String code, OtpType type);
}
