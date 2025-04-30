package com.example.apisample.auth.service;

public interface EmailService {
    void sendOtpEmail(String to, String otp);
    void sendPasswordEmail(String to, String password);
}
