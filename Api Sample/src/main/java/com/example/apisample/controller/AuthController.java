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
import com.example.apisample.model.dto.authdto.RefreshTokenRequestDTO;
import com.example.apisample.model.dto.message.LogMessage;
import com.example.apisample.model.dto.message.ResponseMessage;
import com.example.apisample.model.dto.otpdto.OtpRequestDTO;
import com.example.apisample.service.Interface.JWTService;
import com.example.apisample.service.Interface.OtpService;
import com.example.apisample.service.Interface.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
@Slf4j
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

    @GetMapping("/refresh")
    public ResponseEntity<ResponseObject> refreshToken(@RequestBody RefreshTokenRequestDTO dto) throws Exception {
        log.info(LogMessage.logStartRefresh);

        ResponseObject responseObject = new ResponseObject();

        String accessToken = jwtService.generateAccessToken(dto.getToken());

        if (dto.getToken() != null && !dto.getToken().isBlank()) {
            log.info(LogMessage.logReturningToken);

            responseObject.setStatusCode(HttpStatus.OK.value());
            responseObject.setMessage(ResponseMessage.msgTokenRefresh);
            responseObject.setToken(accessToken);

            log.info(LogMessage.logSuccessRefresh);

            return ResponseEntity.ok(responseObject);
        }

        responseObject.setStatusCode(HttpStatus.UNAUTHORIZED.value());
        responseObject.setMessage(ResponseMessage.msgUnauthorized);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value())
                .body(responseObject);
    }

}
