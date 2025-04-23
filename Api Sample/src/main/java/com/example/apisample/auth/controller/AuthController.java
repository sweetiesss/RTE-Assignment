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
import com.example.apisample.user.model.dto.UserRegisterRequestDTO;
import com.example.apisample.user.service.UserService;
import com.example.apisample.utils.ApiResponse;
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
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final JWTService jwtService;
    private final OtpService otpService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> signIn(@RequestBody @Valid LoginRequestDTO loginUser) {
        log.debug(LogMessage.AUTH_LOGIN_START);

        authService.login(loginUser.getEmail(), loginUser.getPassword());

        log.info(LogMessage.AUTH_LOGIN_SUCCESS);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(ResponseMessage.msgOtpSent)
                        .build()
        );
    }

    @PostMapping("/login-verify-otp")
    public ResponseEntity<ApiResponse> verifyOtp(
            @RequestBody LoginOtpRequestDTO otpRequest,
            HttpServletResponse response
    ) {
        log.debug(LogMessage.USER_GET_BY_EMAIL_START);
        User user = userService.getUserByEmail(otpRequest.getEmail());
        log.debug(LogMessage.USER_GET_BY_EMAIL_SUCCESS);

        log.debug(LogMessage.OTP_VERIFY_START);
        otpService.validateOtp(user, otpRequest.getOtp(), OtpType.LOGIN_2FA);
        log.info(LogMessage.OTP_VERIFY_SUCCESS);

        jwtService.generateRefreshToken(response, user);
        String accessToken = jwtService.generateAccessToken(user);
        log.info(LogMessage.AUTH_TOKEN_GENERATED);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(ResponseMessage.msgSuccess)
                        .data(new LoginResponseDTO(accessToken))
                        .build()
        );
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse> refreshToken(HttpServletRequest request) throws TokenExpiredException {
        log.debug(LogMessage.AUTH_REFRESH_START);

        ApiResponse apiResponse = new ApiResponse();

        String newAccessToken = jwtService.generateAccessTokenFromCookie(request);

        log.debug(LogMessage.AUTH_RETURNING_TOKEN);
        apiResponse.setStatusCode(HttpStatus.OK.value());
        apiResponse.setMessage(ResponseMessage.msgTokenRefresh);
        apiResponse.setData(newAccessToken);
        log.info(LogMessage.AUTH_REFRESH_SUCCESS);

        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/reset-password-request")
    public ResponseEntity<ApiResponse> resetPasswordRequest(@RequestBody @Valid ResetPasswordRequestDTO dto) {
        User user = userService.getUserByEmail(dto.getEmail());

        log.debug(LogMessage.AUTH_RESET_PASSWORD_START);

        if (dto.getOtp() == null || dto.getOtp().isBlank()) {
            log.debug(LogMessage.OTP_RESET_PASSWORD_SENT);

            otpService.generateOtp(user, OtpType.PASSWORD_RESET);

            log.info(LogMessage.OTP_RESET_PASSWORD_SENT_SUCCESS);

            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .statusCode(HttpStatus.OK.value())
                            .message(ResponseMessage.msgOtpSent)
                            .build());
        }

        authService.resetPassword(dto, user);

        log.info(LogMessage.AUTH_RESET_PASSWORD_SUCCESS);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(ResponseMessage.msgResetPasswordSuccess)
                        .build());
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(HttpServletResponse response, @AuthenticationPrincipal User user) {
        log.debug(LogMessage.AUTH_LOGOUT_START);

        authService.logout(response, user);

        log.debug(LogMessage.AUTH_LOGOUT_COOKIE_DELETE);

        log.info(LogMessage.AUTH_LOGOUT_SUCCESS);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(ResponseMessage.msgLogoutSuccess)
                        .build()
        );
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody @Valid UserRegisterRequestDTO dto) {
        log.debug(LogMessage.AUTH_REGISTER_START);

        userService.register(dto);

        log.info(LogMessage.AUTH_REGISTER_SUCCESS);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(ResponseMessage.msgSuccess)
                        .build());
    }
}
