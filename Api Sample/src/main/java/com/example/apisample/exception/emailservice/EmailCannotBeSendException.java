package com.example.apisample.exception.emailservice;

import com.example.apisample.model.dto.message.ResponseMessage;

public class EmailCannotBeSendException extends Exception{
    @Override
    public String getMessage() {
      return ResponseMessage.msgEmailCannotSend;
    }
}
