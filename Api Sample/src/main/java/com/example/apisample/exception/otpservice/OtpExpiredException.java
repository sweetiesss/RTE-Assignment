package com.example.apisample.exception.otpservice;

import com.example.apisample.model.dto.message.ResponseMessage;

public class OtpExpiredException extends Exception {
    @Override
    public String getMessage() {
        return ResponseMessage.msgOtpExpired;
    }
}
