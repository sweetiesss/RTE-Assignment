package com.example.apisample.service.Implement;

import com.example.apisample.exception.emailservice.EmailCannotBeSendException;
import com.example.apisample.model.dto.message.LogMessage;
import com.example.apisample.service.Interface.EmailService;
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

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void sendOtpEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject("OTP Code");
        message.setText("Your OTP is: " + otp + "\nIt will expire in 2 minutes.");

        mailSender.send(message);
    }

    @Override
    public void sendPasswordEmail(String to, String password) throws EmailCannotBeSendException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject("Register Password");
        message.setText("Your password is: " + password + "\nPlease change it immediately");

        try{
            mailSender.send(message);
        }catch (Exception e){
            log.error(LogMessage.logEmailSendFailed);
            throw new EmailCannotBeSendException();
        }
    }
}
