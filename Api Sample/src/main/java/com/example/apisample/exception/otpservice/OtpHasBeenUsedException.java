package com.example.apisample.exception.otpservice;

import com.example.apisample.model.dto.message.ResponseMessage;

public class OtpHasBeenUsedException extends Exception {
    @Override
    public String getMessage() {
        return ResponseMessage.msgOtpHasBeenUsed;
    }
}
