package com.example.apisample.exception.userservice;

import com.example.apisample.model.dto.message.ResponseMessage;


public class UserDoesNotExistException extends Exception{
    @Override
    public String getMessage() {
        return ResponseMessage.msgUserDoesNotExist;
    }
}