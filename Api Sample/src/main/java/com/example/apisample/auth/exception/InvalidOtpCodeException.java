package com.example.apisample.auth.exception;

import com.example.apisample.utils.message.ResponseMessage;

public class InvalidOtpCodeException extends RuntimeException {
    @Override
    public String getMessage() {
        return ResponseMessage.msgOtpInvalid;
    }
}
