package com.example.apisample.exception.ratingservice;

import com.example.apisample.model.dto.message.ResponseMessage;

public class RatingHasBeenRestoreException extends Exception{
    @Override
    public String getMessage() {
        return ResponseMessage.msgRatingHasBeenRestore;
    }
}
