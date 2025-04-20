package com.example.apisample.exception.productservice;

import com.example.apisample.model.dto.message.ResponseMessage;

public class ProductNotFoundException extends Exception{
  @Override
  public String getMessage() {
    return ResponseMessage.msgProductNotFound;
  }
}
