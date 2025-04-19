package com.example.apisample.exception;

import com.example.apisample.exception.emailservice.EmailCannotBeSendException;
import com.example.apisample.exception.jwtservice.InvalidCredentialsException;
import com.example.apisample.exception.jwtservice.InvalidateException;
import com.example.apisample.exception.jwtservice.RoleDoesNotExistException;
import com.example.apisample.exception.jwtservice.TokenExpiredException;
import com.example.apisample.exception.otpservice.InvalidOtpCodeException;
import com.example.apisample.exception.otpservice.OtpDoesNotExistException;
import com.example.apisample.exception.otpservice.OtpExpiredException;
import com.example.apisample.exception.otpservice.OtpHasBeenUsedException;
import com.example.apisample.exception.userservice.*;
import com.example.apisample.model.ResponseObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AccountSuspendedException.class)
    public ResponseEntity<?> handleAccountSuspendedException(AccountSuspendedException ex) {
        String errorMessage = ex.getMessage();

        logger.error(errorMessage);

        ResponseObject responseObject = ResponseObject.builder()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .message(errorMessage)
                .build();

        return new ResponseEntity<>(responseObject, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(InvalidateException.class)
    public ResponseEntity<?> handleTokenExpiredException(InvalidateException ex) {
        String errorMessage = ex.getMessage();

        logger.error(errorMessage);

        ResponseObject responseObject = ResponseObject.builder()
                .statusCode(HttpStatus.PRECONDITION_FAILED.value())
                .message(errorMessage)
                .build();

        return new ResponseEntity<>(responseObject, HttpStatus.PRECONDITION_FAILED);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<?> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        String errorMessage = ex.getMessage();

        logger.error(errorMessage);

        ResponseObject responseObject = ResponseObject.builder()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .message(errorMessage)
                .build();

        return new ResponseEntity<>(responseObject, HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(RoleDoesNotExistException.class)
    public ResponseEntity<?> handleRoleDoesNotExistException(RoleDoesNotExistException ex) {
        String errorMessage = ex.getMessage();

        logger.error(errorMessage);

        ResponseObject responseObject = ResponseObject.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(errorMessage)
                .build();

        return new ResponseEntity<>(responseObject, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<?> handleTokenExpiredException(TokenExpiredException ex) {
        String errorMessage = ex.getMessage();

        logger.error(errorMessage);

        ResponseObject responseObject = ResponseObject.builder()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .message(errorMessage)
                .build();

        return new ResponseEntity<>(responseObject, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        String errorMessage = ex.getMessage();

        logger.error(errorMessage);

        ResponseObject responseObject = ResponseObject.builder()
                .statusCode(HttpStatus.CONFLICT.value())
                .message(errorMessage)
                .build();

        return new ResponseEntity<>(responseObject, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserDeletedException.class)
    public ResponseEntity<?> handleUserDeletedException(UserDeletedException ex) {
        String errorMessage = ex.getMessage();

        logger.error(errorMessage);

        ResponseObject responseObject = ResponseObject.builder()
                .statusCode(HttpStatus.GONE.value())
                .message(errorMessage)
                .build();

        return new ResponseEntity<>(responseObject, HttpStatus.GONE);
    }

    @ExceptionHandler(UserDoesNotExistException.class)
    public ResponseEntity<?> handleUserDoesNotExistException(UserDoesNotExistException ex) {
        String errorMessage = ex.getMessage();

        logger.error(errorMessage);

        ResponseObject responseObject = ResponseObject.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(errorMessage)
                .build();

        return new ResponseEntity<>(responseObject, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDenied(AccessDeniedException ex) {
        String errorMessage = ex.getMessage();

        logger.error(errorMessage);

        ResponseObject responseObject = ResponseObject.builder()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .message(errorMessage)
                .build();

        return new ResponseEntity<>(responseObject, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(InvalidOtpCodeException.class)
    public ResponseEntity<?> handleInvalidOtpCodeException(InvalidOtpCodeException ex) {
        String errorMessage = ex.getMessage();
        logger.error(errorMessage);
        ResponseObject responseObject = ResponseObject.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(errorMessage)
                .build();
        return new ResponseEntity<>(responseObject, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OtpDoesNotExistException.class)
    public ResponseEntity<?> handleOtpDoesNotExistException(OtpDoesNotExistException ex) {
        String errorMessage = ex.getMessage();
        logger.error(errorMessage);
        ResponseObject responseObject = ResponseObject.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(errorMessage)
                .build();
        return new ResponseEntity<>(responseObject, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OtpExpiredException.class)
    public ResponseEntity<?> handleOtpExpiredException(OtpExpiredException ex) {
        String errorMessage = ex.getMessage();
        logger.error(errorMessage);
        ResponseObject responseObject = ResponseObject.builder()
                .statusCode(HttpStatus.GONE.value())
                .message(errorMessage)
                .build();
        return new ResponseEntity<>(responseObject, HttpStatus.GONE);
    }

    @ExceptionHandler(OtpHasBeenUsedException.class)
    public ResponseEntity<?> handleOtpHasBeenUsedException(OtpHasBeenUsedException ex) {
        String errorMessage = ex.getMessage();
        logger.error(errorMessage);
        ResponseObject responseObject = ResponseObject.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(errorMessage)
                .build();
        return new ResponseEntity<>(responseObject, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailCannotBeSendException.class)
    public ResponseEntity<?> handleEmailCannotBeSendException(EmailCannotBeSendException ex) {
        String errorMessage = ex.getMessage();
        logger.error(errorMessage);
        ResponseObject responseObject = ResponseObject.builder()
                .statusCode(HttpStatus.SERVICE_UNAVAILABLE.value())
                .message(errorMessage)
                .build();
        return new ResponseEntity<>(responseObject, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(UserDoesNotLoginException.class)
    public ResponseEntity<?> handleUserDoesNotLoginException(UserDoesNotLoginException ex) {
        String errorMessage = ex.getMessage();
        logger.error(errorMessage);
        ResponseObject responseObject = ResponseObject.builder()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .message(errorMessage)
                .build();
        return new ResponseEntity<>(responseObject, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NotImageFileException.class)
    public ResponseEntity<?> handleNotImageFileException(NotImageFileException ex) {
        String errorMessage = ex.getMessage();
        logger.error(errorMessage);
        ResponseObject responseObject = ResponseObject.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(errorMessage)
                .build();
        return new ResponseEntity<>(responseObject, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> BaseException(Exception ex) {
        String errorMessage = ex.getMessage();
        logger.error(errorMessage);
        ResponseObject responseObject = ResponseObject.builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(errorMessage)
                .build();
        return new ResponseEntity<>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}