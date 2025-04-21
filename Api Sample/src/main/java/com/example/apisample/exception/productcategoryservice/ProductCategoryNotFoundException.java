package com.example.apisample.exception.productcategoryservice;

import com.example.apisample.model.dto.message.ResponseMessage;

public class ProductCategoryNotFoundException extends Exception {
    @Override
    public String getMessage() {
        return ResponseMessage.msgProductCategoryNotFound;
    }
}