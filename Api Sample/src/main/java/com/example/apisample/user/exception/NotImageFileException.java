package com.example.apisample.user.exception;
import com.example.apisample.utils.message.ResponseMessage;


public class NotImageFileException extends RuntimeException {

    @Override
    public String getMessage() {
        return ResponseMessage.msgNotImageFile;
    }
}
