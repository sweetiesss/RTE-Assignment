package com.example.apisample.auth.service.impl;

import com.example.apisample.auth.exception.EmailCannotBeSendException;
import com.example.apisample.auth.service.EmailService;
import com.example.apisample.config.MailTemplateBuilder;
import com.example.apisample.utils.message.LogMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImp implements EmailService {

    private final JavaMailSender mailSender;
    private final MailTemplateBuilder templateBuilder;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void sendOtpEmail(String to, String otp) {
        SimpleMailMessage message = templateBuilder.buildOtpEmail(from, to, otp);
        mailSender.send(message);
    }

    @Override
    public void sendPasswordEmail(String to, String password) {
        try {
            SimpleMailMessage message = templateBuilder.buildPasswordEmail(from, to, password);
            mailSender.send(message);
        } catch (Exception e) {
            log.error(LogMessage.logEmailSendFailed);
            throw new EmailCannotBeSendException();
        }
    }

}
