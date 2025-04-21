package com.example.apisample.exception.categoryservice;

import com.example.apisample.model.dto.message.ResponseMessage;

public class CategoryNotFoundException extends Exception {
  @Override
  public String getMessage() {
    return ResponseMessage.msgCategoryNotFound;
  }
}
