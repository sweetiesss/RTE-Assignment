package com.example.apisample.service.Interface;

import com.example.apisample.entity.User;
import com.example.apisample.enums.OtpType;
import com.example.apisample.exception.otpservice.InvalidOtpCodeException;
import com.example.apisample.exception.otpservice.OtpDoesNotExistException;
import com.example.apisample.exception.otpservice.OtpExpiredException;
import com.example.apisample.exception.otpservice.OtpHasBeenUsedException;

public interface OtpService {
    void generateOtp(User user, OtpType type);
    void validateOtp(User user, String code, OtpType type) throws OtpDoesNotExistException, OtpExpiredException, InvalidOtpCodeException, OtpHasBeenUsedException;
}
