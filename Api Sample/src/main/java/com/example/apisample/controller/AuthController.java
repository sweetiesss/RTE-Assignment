package com.example.apisample.controller;

import com.example.apisample.entity.User;
import com.example.apisample.enums.OtpType;
import com.example.apisample.exception.userservice.UserDoesNotLoginException;
import com.example.apisample.model.ResponseObject;
import com.example.apisample.model.dto.auth.LoginRequestDTO;
import com.example.apisample.model.dto.auth.LoginResponseDTO;
import com.example.apisample.model.dto.auth.ResetPasswordRequestDTO;
import com.example.apisample.model.dto.message.LogMessage;
import com.example.apisample.model.dto.message.ResponseMessage;
import com.example.apisample.model.dto.otp.LoginOtpRequestDTO;
import com.example.apisample.model.dto.user.UserRegisterRequestDTO;
import com.example.apisample.service.Interface.JWTService;
import com.example.apisample.service.Interface.OtpService;
import com.example.apisample.service.Interface.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

    private final UserService userService;
    private final JWTService jwtService;
    private final OtpService otpService;
    private final String REFRESH_TOKEN_COOKIE_NAME = "refresh-token";

    @PostMapping("/login")
    public ResponseEntity<ResponseObject> signIn(@RequestBody LoginRequestDTO loginUser) throws Exception {
        userService.login(loginUser.getEmail(), loginUser.getPassword());

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

        String refreshToken = jwtService.generateRefreshToken(user);
        String accessToken = jwtService.generateAccessToken(refreshToken);

        Cookie refreshTokenCookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60);

        response.addCookie(refreshTokenCookie);

        return ResponseEntity.ok(
                ResponseObject.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(ResponseMessage.msgSuccess)
                        .token(new LoginResponseDTO(accessToken))
                        .build()
        );
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ResponseObject> refreshToken(HttpServletRequest request) throws Exception {
        log.info(LogMessage.logStartRefresh);

        ResponseObject responseObject = new ResponseObject();

        String refreshToken = null;

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(REFRESH_TOKEN_COOKIE_NAME)) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        String newAccessToken = jwtService.generateAccessToken(refreshToken);

        log.info(LogMessage.logReturningToken);
        responseObject.setStatusCode(HttpStatus.OK.value());
        responseObject.setMessage(ResponseMessage.msgTokenRefresh);
        responseObject.setToken(newAccessToken);
        log.info(LogMessage.logSuccessRefresh);

        return ResponseEntity.ok(responseObject);
    }


    @PostMapping("/reset-password-request")
    public ResponseEntity<ResponseObject> resetPasswordRequest(@RequestBody ResetPasswordRequestDTO dto) throws Exception {
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

        userService.resetPassword(dto, user);
        log.info(LogMessage.logSuccessResetPassword);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(ResponseMessage.msgResetPasswordSuccess)
                        .build());
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseObject> logout(HttpServletResponse response, @AuthenticationPrincipal User user) throws UserDoesNotLoginException {
        log.info(LogMessage.logStartLogout);

        userService.logout(user);

        log.info(LogMessage.logStartLogoutCookieDelete);

        Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        response.addCookie(cookie);

        log.info(LogMessage.logSuccessLogout);

        return ResponseEntity.ok(
                ResponseObject.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(ResponseMessage.msgLogoutSuccess)
                        .build()
        );
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseObject> register(@RequestBody UserRegisterRequestDTO dto) throws Exception {

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
