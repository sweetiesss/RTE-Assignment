package com.example.apisample.exception.jwtservice;


import com.example.apisample.model.dto.message.ResponseMessage;

public class InvalidCredentialsException extends Exception{
    @Override
    public String getMessage() {
        return ResponseMessage.msgInvalidCredential;
    }
}