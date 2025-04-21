package com.example.apisample.auth.exception;

import com.example.apisample.utils.message.ResponseMessage;

public class EmailCannotBeSendException extends RuntimeException{
    @Override
    public String getMessage() {
      return ResponseMessage.msgEmailCannotSend;
    }
}
