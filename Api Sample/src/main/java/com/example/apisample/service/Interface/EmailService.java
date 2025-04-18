package com.example.apisample.service.Interface;

import com.example.apisample.exception.emailservice.EmailCannotBeSendException;

public interface EmailService {
    void sendOtpEmail(String to, String otp);
    void sendPasswordEmail(String to, String password) throws EmailCannotBeSendException;
}
