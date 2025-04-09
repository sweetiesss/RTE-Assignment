package com.example.apisample.exception.jwtservice;
import com.example.apisample.model.dto.message.ResponseMessage;

public class RoleDoesNotExistException extends Exception{
    @Override
    public String getMessage() {
        return ResponseMessage.msgRoleDoesNotExist;
    }
}