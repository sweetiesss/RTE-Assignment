package com.example.apisample.auth.exception;

import com.example.apisample.utils.message.ResponseMessage;

public class OtpHasBeenUsedException extends RuntimeException {
    @Override
    public String getMessage() {
        return ResponseMessage.msgOtpHasBeenUsed;
    }
}
