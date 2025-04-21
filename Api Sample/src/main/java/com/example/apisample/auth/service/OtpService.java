package com.example.apisample.auth.service;

import com.example.apisample.auth.enums.OtpType;
import com.example.apisample.auth.exception.InvalidOtpCodeException;
import com.example.apisample.auth.exception.OtpDoesNotExistException;
import com.example.apisample.auth.exception.OtpExpiredException;
import com.example.apisample.auth.exception.OtpHasBeenUsedException;
import com.example.apisample.user.entity.User;

public interface OtpService {
    void generateOtp(User user, OtpType type);
    void validateOtp(User user, String code, OtpType type);
}
