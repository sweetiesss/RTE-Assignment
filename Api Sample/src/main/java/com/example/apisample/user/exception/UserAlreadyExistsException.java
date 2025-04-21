package com.example.apisample.user.exception;
import com.example.apisample.utils.message.ResponseMessage;


public class UserAlreadyExistsException extends RuntimeException {
    @Override
    public String getMessage() {
        return ResponseMessage.msgUserAlreadyExist;
    }
}