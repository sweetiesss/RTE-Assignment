package com.example.apisample.auth.service;

import com.example.apisample.auth.exception.EmailCannotBeSendException;

public interface EmailService {
    void sendOtpEmail(String to, String otp);
    void sendPasswordEmail(String to, String password);
}
