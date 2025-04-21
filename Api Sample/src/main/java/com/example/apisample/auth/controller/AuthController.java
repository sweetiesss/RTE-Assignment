package com.example.apisample.auth.controller;

import com.example.apisample.auth.dto.LoginOtpRequestDTO;
import com.example.apisample.auth.dto.LoginRequestDTO;
import com.example.apisample.auth.dto.LoginResponseDTO;
import com.example.apisample.auth.dto.ResetPasswordRequestDTO;
import com.example.apisample.auth.enums.OtpType;
import com.example.apisample.auth.exception.TokenExpiredException;
import com.example.apisample.auth.service.AuthService;
import com.example.apisample.auth.service.JWTService;
import com.example.apisample.auth.service.OtpService;
import com.example.apisample.user.entity.User;
import com.example.apisample.user.exception.UserDoesNotLoginException;
import com.example.apisample.user.model.dto.UserRegisterRequestDTO;
import com.example.apisample.user.service.UserService;
import com.example.apisample.utils.ResponseObject;
import com.example.apisample.utils.message.LogMessage;
import com.example.apisample.utils.message.ResponseMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final JWTService jwtService;
    private final OtpService otpService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<ResponseObject> signIn(@RequestBody @Valid LoginRequestDTO loginUser) {
        authService.login(loginUser.getEmail(), loginUser.getPassword());

        return ResponseEntity.ok(
                ResponseObject.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(ResponseMessage.msgOtpSent)
                        .build()
        );
    }

    @PostMapping("/login-verify-otp")
    public ResponseEntity<ResponseObject> verifyOtp(
            @RequestBody LoginOtpRequestDTO otpRequest,
            HttpServletResponse response
    ) throws Exception {

        User user = userService.getUserByEmail(otpRequest.getEmail());
        otpService.validateOtp(user, otpRequest.getOtp(), OtpType.LOGIN_2FA);

        jwtService.generateRefreshToken(response, user);
        String accessToken = jwtService.generateAccessToken(user);

        return ResponseEntity.ok(
                ResponseObject.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(ResponseMessage.msgSuccess)
                        .token(new LoginResponseDTO(accessToken))
                        .build()
        );
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ResponseObject> refreshToken(HttpServletRequest request) throws TokenExpiredException {
        log.info(LogMessage.logStartRefresh);

        ResponseObject responseObject = new ResponseObject();

        String newAccessToken = jwtService.generateAccessTokenFromCookie(request);

        log.info(LogMessage.logReturningToken);
        responseObject.setStatusCode(HttpStatus.OK.value());
        responseObject.setMessage(ResponseMessage.msgTokenRefresh);
        responseObject.setToken(newAccessToken);
        log.info(LogMessage.logSuccessRefresh);

        return ResponseEntity.ok(responseObject);
    }


    @PostMapping("/reset-password-request")
    public ResponseEntity<ResponseObject> resetPasswordRequest(@RequestBody @Valid ResetPasswordRequestDTO dto)  {
        User user = userService.getUserByEmail(dto.getEmail());

        log.info(LogMessage.logStartResetPassword);

        if(dto.getOtp() == null || dto.getOtp().isBlank()){
            log.info(LogMessage.logOtpResetPasswordSent);
            otpService.generateOtp(user, OtpType.PASSWORD_RESET);
            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .statusCode(HttpStatus.OK.value())
                            .message(ResponseMessage.msgOtpSent)
                            .build());
        }

        authService.resetPassword(dto, user);
        log.info(LogMessage.logSuccessResetPassword);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(ResponseMessage.msgResetPasswordSuccess)
                        .build());
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseObject> logout(HttpServletResponse response, @AuthenticationPrincipal User user) {
        log.info(LogMessage.logStartLogout);

        authService.logout(response, user);

        log.info(LogMessage.logStartLogoutCookieDelete);

        log.info(LogMessage.logSuccessLogout);

        return ResponseEntity.ok(
                ResponseObject.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(ResponseMessage.msgLogoutSuccess)
                        .build()
        );
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseObject> register(@RequestBody @Valid UserRegisterRequestDTO dto)  {

        log.info(LogMessage.logStartRegis);

        userService.register(dto);

        log.info(LogMessage.logSuccessRegis);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(ResponseMessage.msgSuccess)
                        .build());
    }
}
