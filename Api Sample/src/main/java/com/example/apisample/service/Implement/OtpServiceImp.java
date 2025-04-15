package com.example.apisample.service.Implement;

import com.example.apisample.entity.OtpCode;
import com.example.apisample.entity.User;
import com.example.apisample.enums.OtpType;
import com.example.apisample.exception.otpservice.InvalidOtpCodeException;
import com.example.apisample.exception.otpservice.OtpDoesNotExistException;
import com.example.apisample.exception.otpservice.OtpExpiredException;
import com.example.apisample.exception.otpservice.OtpHasBeenUsedException;
import com.example.apisample.repository.OtpRepository;
import com.example.apisample.service.Interface.EmailService;
import com.example.apisample.service.Interface.OtpService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpServiceImp implements OtpService {

    private final OtpRepository otpCodeRepository;
    private final EmailService emailService;
    private static final int OTP_LENGTH = 6;
    private static final int OTP_EXPIRATION_MINUTES = 2;


    @Override
    public void generateOtp(User user, OtpType type) {
        String otp = generateRandomOtp();

        OtpCode otpCode = OtpCode.builder()
                .user(user)
                .type(type)
                .code(otp)
                .expiresAt(LocalDateTime.now().plusMinutes(OTP_EXPIRATION_MINUTES))
                .isUsed(false)
                .build();

        otpCodeRepository.save(otpCode);

        emailService.sendOtpEmail(user.getEmail(), otp);
    }

    @Override
    @Transactional
    public void validateOtp(User user, String code, OtpType type) throws OtpDoesNotExistException, OtpExpiredException, InvalidOtpCodeException, OtpHasBeenUsedException {
        OtpCode otpCode = otpCodeRepository
                .findTopByUserAndTypeOrderByExpiresAtDesc(user, type)
                .orElse(null);

        if (otpCode == null) throw new OtpDoesNotExistException();
        if(!otpCode.getCode().equals(code)) throw new InvalidOtpCodeException();
        if(!otpCode.getExpiresAt().isAfter(LocalDateTime.now())) throw new OtpExpiredException();
        if(otpCode.isUsed()) throw new OtpHasBeenUsedException();
        otpCode.setUsed(true);
        otpCodeRepository.save(otpCode);
    }

    private String generateRandomOtp() {
        Random random = new Random();
        int number = 100000 + random.nextInt(900000); // 6-digit
        return String.valueOf(number);
    }
}
