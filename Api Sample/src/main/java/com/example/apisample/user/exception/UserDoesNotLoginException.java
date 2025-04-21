package com.example.apisample.user.exception;

import com.example.apisample.utils.message.ResponseMessage;

public class UserDoesNotLoginException extends RuntimeException{
    @Override
    public String getMessage() {
        return ResponseMessage.msgUserDoesNotLoggedIn;
    }
}