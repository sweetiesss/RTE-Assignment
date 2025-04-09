package com.example.apisample.exception.userservice;
import com.example.apisample.model.dto.message.ResponseMessage;


public class UserAlreadyExistsException extends Exception {
    @Override
    public String getMessage() {
        return ResponseMessage.msgUserAlreadyExist;
    }
}