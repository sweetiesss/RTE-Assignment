package com.example.apisample.rating.exception;

import com.example.apisample.utils.message.ResponseMessage;

public class RatingNotFoundException extends RuntimeException{
    @Override
    public String getMessage() {
        return ResponseMessage.msgRatingDeleted;
    }
}
