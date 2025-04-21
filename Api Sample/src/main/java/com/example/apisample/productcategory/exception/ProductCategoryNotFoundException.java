package com.example.apisample.productcategory.exception;

import com.example.apisample.utils.message.ResponseMessage;

public class ProductCategoryNotFoundException extends RuntimeException {
    @Override
    public String getMessage() {
        return ResponseMessage.msgProductCategoryNotFound;
    }
}