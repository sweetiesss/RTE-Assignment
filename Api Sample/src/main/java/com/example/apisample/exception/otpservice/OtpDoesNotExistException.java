package com.example.apisample.exception.otpservice;

import com.example.apisample.model.dto.message.ResponseMessage;

public class OtpDoesNotExistException extends Exception {
    @Override
    public String getMessage() {
        return ResponseMessage.msgOtpDoesNotExist;
    }
}
