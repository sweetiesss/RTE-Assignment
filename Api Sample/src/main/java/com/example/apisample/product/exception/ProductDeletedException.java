package com.example.apisample.product.exception;

import com.example.apisample.utils.message.ResponseMessage;

public class ProductDeletedException extends RuntimeException{
    @Override
    public String getMessage() {
        return ResponseMessage.msgProductDeleted;
    }
}

