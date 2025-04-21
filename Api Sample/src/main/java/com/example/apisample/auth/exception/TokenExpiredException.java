package com.example.apisample.auth.exception;
import com.example.apisample.utils.message.ResponseMessage;

public class TokenExpiredException extends Exception {
    @Override
    public String getMessage() {
        return ResponseMessage.msgTokenExpired;
    }
}