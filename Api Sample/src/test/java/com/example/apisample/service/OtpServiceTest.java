package com.example.apisample.service;

import com.example.apisample.auth.entity.OtpCode;
import com.example.apisample.auth.enums.OtpType;
import com.example.apisample.auth.exception.InvalidOtpCodeException;
import com.example.apisample.auth.exception.OtpDoesNotExistException;
import com.example.apisample.auth.exception.OtpExpiredException;
import com.example.apisample.auth.exception.OtpHasBeenUsedException;
import com.example.apisample.auth.repository.OtpRepository;
import com.example.apisample.auth.service.EmailService;
import com.example.apisample.auth.service.impl.OtpServiceImp;
import com.example.apisample.role.entity.Role;
import com.example.apisample.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OtpServiceTest {

    @Mock
    private OtpRepository otpRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private OtpServiceImp otpService;

    private User user;

    @BeforeEach
    void setup() {
        Role role = Role.builder().id(1).roleName("USER").build();

        user = User.builder()
                .id(1)
                .email("test@example.com")
                .password("password")
                .role(role)
                .build();
    }

    @Test
    void testGenerateOtp_sendsEmailAndSavesOtp() {
        otpService.generateOtp(user, OtpType.EMAIL_VERIFICATION);

        verify(otpRepository, times(1)).save(any(OtpCode.class));
        verify(emailService, times(1)).sendOtpEmail(eq(user.getEmail()), anyString());
    }

    @Test
    void testValidateOtp_validCode_marksAsUsedAndSaves() {
        OtpCode otp = OtpCode.builder()
                .user(user)
                .code("123456")
                .type(OtpType.EMAIL_VERIFICATION)
                .expiresAt(LocalDateTime.now().plusMinutes(2))
                .used(false)
                .build();

        when(otpRepository.findTopByUserAndTypeOrderByExpiresAtDesc(user, OtpType.EMAIL_VERIFICATION))
                .thenReturn(Optional.of(otp));

        otpService.validateOtp(user, "123456", OtpType.EMAIL_VERIFICATION);

        assertTrue(otp.isUsed());
        verify(otpRepository).save(otp);
    }

    @Test
    void testValidateOtp_otpDoesNotExist_throwsOtpDoesNotExistException() {
        when(otpRepository.findTopByUserAndTypeOrderByExpiresAtDesc(user, OtpType.EMAIL_VERIFICATION))
                .thenReturn(Optional.empty());

        assertThrows(OtpDoesNotExistException.class, () -> {
            otpService.validateOtp(user, "123456", OtpType.EMAIL_VERIFICATION);
        });
    }

    @Test
    void testValidateOtp_invalidCode_throwsInvalidOtpCodeException() {
        OtpCode otp = OtpCode.builder()
                .user(user)
                .code("123456")
                .type(OtpType.EMAIL_VERIFICATION)
                .expiresAt(LocalDateTime.now().plusMinutes(2))
                .used(false)
                .build();

        when(otpRepository.findTopByUserAndTypeOrderByExpiresAtDesc(user, OtpType.EMAIL_VERIFICATION))
                .thenReturn(Optional.of(otp));

        assertThrows(InvalidOtpCodeException.class, () -> {
            otpService.validateOtp(user, "654321", OtpType.EMAIL_VERIFICATION);
        });
    }

    @Test
    void testValidateOtp_otpExpired_throwsOtpExpiredException() {
        OtpCode otp = OtpCode.builder()
                .user(user)
                .code("123456")
                .type(OtpType.EMAIL_VERIFICATION)
                .expiresAt(LocalDateTime.now().minusMinutes(1)) // Expired OTP
                .used(false)
                .build();

        when(otpRepository.findTopByUserAndTypeOrderByExpiresAtDesc(user, OtpType.EMAIL_VERIFICATION))
                .thenReturn(Optional.of(otp));

        assertThrows(OtpExpiredException.class, () -> {
            otpService.validateOtp(user, "123456", OtpType.EMAIL_VERIFICATION);
        });
    }

    @Test
    void testValidateOtp_otpUsed_throwsOtpHasBeenUsedException() {
        OtpCode otp = OtpCode.builder()
                .user(user)
                .code("123456")
                .type(OtpType.EMAIL_VERIFICATION)
                .expiresAt(LocalDateTime.now().plusMinutes(2))
                .used(true) // OTP has already been used
                .build();

        when(otpRepository.findTopByUserAndTypeOrderByExpiresAtDesc(user, OtpType.EMAIL_VERIFICATION))
                .thenReturn(Optional.of(otp));

        assertThrows(OtpHasBeenUsedException.class, () -> {
            otpService.validateOtp(user, "123456", OtpType.EMAIL_VERIFICATION);
        });
    }

    @Test
    void testCleanExpiredOtps_deletesExpiredRecords() {
        otpService.cleanExpiredOtps();

        verify(otpRepository, times(1)).deleteByExpiresAtBefore(any(LocalDateTime.class));
    }
}
