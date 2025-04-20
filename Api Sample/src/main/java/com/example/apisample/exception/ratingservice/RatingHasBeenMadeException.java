package com.example.apisample.exception.ratingservice;

import com.example.apisample.model.dto.message.ResponseMessage;

public class RatingHasBeenMadeException extends Exception{
  @Override
  public String getMessage() {
    return ResponseMessage.msgRatingHasBeenMade;
  }
}