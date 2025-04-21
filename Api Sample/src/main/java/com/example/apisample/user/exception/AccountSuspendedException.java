package com.example.apisample.user.exception;

import com.example.apisample.utils.message.ResponseMessage;

public class AccountSuspendedException extends RuntimeException{
    @Override
    public String getMessage() {
        return ResponseMessage.msgAccountSuspended;
    }
}