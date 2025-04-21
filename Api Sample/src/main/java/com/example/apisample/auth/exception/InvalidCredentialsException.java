package com.example.apisample.auth.exception;


import com.example.apisample.utils.message.ResponseMessage;

public class InvalidCredentialsException extends RuntimeException{
    @Override
    public String getMessage() {
        return ResponseMessage.msgInvalidCredential;
    }
}