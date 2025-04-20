package com.example.apisample.exception.productservice;

import com.example.apisample.model.dto.message.ResponseMessage;

public class ProductDeletedException extends Exception{
    @Override
    public String getMessage() {
        return ResponseMessage.msgProductDeleted;
    }
}

