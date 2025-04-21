package com.example.apisample.config;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component
public class MailTemplateBuilder {
    public SimpleMailMessage buildOtpEmail(String from, String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject("OTP Code");
        message.setText("Your OTP is: " + otp + "\nIt will expire in 2 minutes.");
        return message;
    }

    public SimpleMailMessage buildPasswordEmail(String from, String to, String password) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject("Register Password");
        message.setText("Your password is: " + password + "\nPlease change it immediately");
        return message;
    }
}
