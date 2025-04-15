package com.example.apisample.controller;

import com.example.apisample.entity.User;
import com.example.apisample.enums.OtpType;
import com.example.apisample.exception.jwtservice.TokenExpiredException;
import com.example.apisample.exception.otpservice.InvalidOtpCodeException;
import com.example.apisample.exception.otpservice.OtpDoesNotExistException;
import com.example.apisample.exception.otpservice.OtpExpiredException;
import com.example.apisample.exception.otpservice.OtpHasBeenUsedException;
import com.example.apisample.exception.userservice.InvalidateException;
import com.example.apisample.exception.userservice.UserDoesNotExistException;
import com.example.apisample.model.ResponseObject;
import com.example.apisample.model.dto.authdto.LoginRequestDTO;
import com.example.apisample.model.dto.authdto.LoginResponseDTO;
import com.example.apisample.model.dto.message.ResponseMessage;
import com.example.apisample.model.dto.otpdto.OtpRequestDTO;
import com.example.apisample.service.Interface.JWTService;
import com.example.apisample.service.Interface.OtpService;
import com.example.apisample.service.Interface.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;
    private final JWTService jwtService;
    private final OtpService otpService;

    @PostMapping("/login")
    public ResponseEntity<ResponseObject> signIn(@RequestBody LoginRequestDTO loginUser) throws Exception {
        User user = userService.login(loginUser.getEmail(), loginUser.getPassword());

        otpService.generateOtp(user, OtpType.LOGIN_2FA);

        return ResponseEntity.ok(
                ResponseObject.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(ResponseMessage.msgOtpSent)
                        .build()
        );
    }

    // Step 3: OTP validation — user enters OTP
    @PostMapping("/verify-otp")
    public ResponseEntity<ResponseObject> verifyOtp(@RequestBody OtpRequestDTO otpRequest) throws TokenExpiredException, UserDoesNotExistException, InvalidateException, OtpDoesNotExistException, InvalidOtpCodeException, OtpHasBeenUsedException, OtpExpiredException {
        User user = userService.getUserByEmail(otpRequest.getEmail());

        otpService.validateOtp(user, otpRequest.getOtp(), otpRequest.getType());

        String refreshToken = jwtService.generateRefreshToken(user);
        String accessToken = jwtService.generateAccessToken(refreshToken);

        return ResponseEntity.ok(
                ResponseObject.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(ResponseMessage.msgSuccess)
                        .token(new LoginResponseDTO(accessToken, refreshToken))
                        .build()
        );
    }
}
