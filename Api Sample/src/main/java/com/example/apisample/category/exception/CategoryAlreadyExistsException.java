package com.example.apisample.category.exception;

import com.example.apisample.utils.message.ResponseMessage;

public class CategoryAlreadyExistsException extends RuntimeException {
  @Override
  public String getMessage() {
    return ResponseMessage.msgCategoryAlreadyExists;
  }
}
