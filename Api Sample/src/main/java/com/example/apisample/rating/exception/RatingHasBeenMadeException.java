package com.example.apisample.rating.exception;

import com.example.apisample.utils.message.ResponseMessage;

public class RatingHasBeenMadeException extends RuntimeException{
  @Override
  public String getMessage() {
    return ResponseMessage.msgRatingHasBeenMade;
  }
}