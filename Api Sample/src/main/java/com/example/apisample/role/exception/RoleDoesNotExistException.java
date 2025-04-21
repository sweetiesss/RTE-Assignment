package com.example.apisample.role.exception;
import com.example.apisample.utils.message.ResponseMessage;

public class RoleDoesNotExistException extends RuntimeException{
    @Override
    public String getMessage() {
        return ResponseMessage.msgRoleDoesNotExist;
    }
}