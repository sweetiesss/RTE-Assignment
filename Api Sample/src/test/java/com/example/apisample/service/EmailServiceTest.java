package com.example.apisample.service;

import com.example.apisample.auth.exception.EmailCannotBeSendException;
import com.example.apisample.auth.service.impl.EmailServiceImp;
import com.example.apisample.config.MailTemplateBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MailTemplateBuilder templateBuilder;

    @InjectMocks
    private EmailServiceImp emailService;

    private String from;
    private String to;
    private String otp;
    private String password;

    @BeforeEach
    void setup() throws Exception{
        from = "test@example.com";
        to = "user@example.com";
        otp = "123456";
        password = "newpassword123";

        Field fromField = EmailServiceImp.class.getDeclaredField("from");
        fromField.setAccessible(true);
        fromField.set(emailService, from);
    }

    @Test
    void testSendOtpEmail_success() {
        SimpleMailMessage message = mock(SimpleMailMessage.class);
        when(templateBuilder.buildOtpEmail(from, to, otp)).thenReturn(message);

        doNothing().when(mailSender).send(message); // Mock the send method to do nothing

        emailService.sendOtpEmail(to, otp);

        verify(templateBuilder, times(1)).buildOtpEmail(from, to, otp);
        verify(mailSender, times(1)).send(message); // Ensure send method was called
    }

    @Test
    void testSendOtpEmail_failure_throwsEmailCannotBeSendException() {
        when(templateBuilder.buildOtpEmail(from, to, otp)).thenThrow(new RuntimeException("Email sending failed"));

        assertThrows(EmailCannotBeSendException.class, () -> {
            emailService.sendOtpEmail(to, otp);
        });

        verify(templateBuilder, times(1)).buildOtpEmail(from, to, otp);
        verify(mailSender, times(0)).send(any(SimpleMailMessage.class)); // Verify send was not called
    }

    @Test
    void testSendPasswordEmail_success() {
        SimpleMailMessage message = mock(SimpleMailMessage.class);
        when(templateBuilder.buildPasswordEmail(from, to, password)).thenReturn(message);

        doNothing().when(mailSender).send(message); // Mock the send method to do nothing

        emailService.sendPasswordEmail(to, password);

        verify(templateBuilder, times(1)).buildPasswordEmail(from, to, password);
        verify(mailSender, times(1)).send(message); // Ensure send method was called
    }

    @Test
    void testSendPasswordEmail_failure_throwsEmailCannotBeSendException() {
        when(templateBuilder.buildPasswordEmail(from, to, password)).thenThrow(new RuntimeException("Email sending failed"));

        assertThrows(EmailCannotBeSendException.class, () -> {
            emailService.sendPasswordEmail(to, password);
        });

        verify(templateBuilder, times(1)).buildPasswordEmail(from, to, password);
        verify(mailSender, times(0)).send(any(SimpleMailMessage.class)); // Verify send was not called
    }
}
