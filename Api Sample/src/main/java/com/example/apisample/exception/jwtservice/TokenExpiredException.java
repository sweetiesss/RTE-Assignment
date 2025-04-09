package com.example.apisample.exception.jwtservice;
import com.example.apisample.model.dto.message.ResponseMessage;

public class TokenExpiredException extends Exception {
    @Override
    public String getMessage() {
        return ResponseMessage.msgTokenExpired;
    }
}