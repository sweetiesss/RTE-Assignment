package com.example.apisample.category.exception;

import com.example.apisample.utils.message.ResponseMessage;

public class CategoryNotFoundException extends RuntimeException {
  @Override
  public String getMessage() {
    return ResponseMessage.msgCategoryNotFound;
  }
}
