package com.example.apisample.utils;

import com.example.apisample.auth.exception.*;
import com.example.apisample.category.exception.CategoryAlreadyExistsException;
import com.example.apisample.category.exception.CategoryNotFoundException;
import com.example.apisample.product.exception.ProductDeletedException;
import com.example.apisample.product.exception.ProductNotFoundException;
import com.example.apisample.productcategory.exception.ProductCategoryNotFoundException;
import com.example.apisample.rating.exception.RatingDeletedException;
import com.example.apisample.rating.exception.RatingHasBeenMadeException;
import com.example.apisample.rating.exception.RatingHasBeenRestoreException;
import com.example.apisample.rating.exception.RatingNotFoundException;
import com.example.apisample.role.exception.RoleDoesNotExistException;
import com.example.apisample.user.exception.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AccountSuspendedException.class)
    public ResponseEntity<?> handleAccountSuspendedException(AccountSuspendedException ex) {
        String errorMessage = ex.getMessage();

        logger.error(errorMessage, ex);

        ApiResponse apiResponse = ApiResponse.builder()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .message(errorMessage)
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(InvalidateException.class)
    public ResponseEntity<?> handleTokenExpiredException(InvalidateException ex) {
        String errorMessage = ex.getMessage();

        logger.error(errorMessage, ex);

        ApiResponse apiResponse = ApiResponse.builder()
                .statusCode(HttpStatus.PRECONDITION_FAILED.value())
                .message(errorMessage)
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.PRECONDITION_FAILED);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<?> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        String errorMessage = ex.getMessage();

        logger.error(errorMessage, ex);

        ApiResponse apiResponse = ApiResponse.builder()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .message(errorMessage)
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(RoleDoesNotExistException.class)
    public ResponseEntity<?> handleRoleDoesNotExistException(RoleDoesNotExistException ex) {
        String errorMessage = ex.getMessage();

        logger.error(errorMessage, ex);

        ApiResponse apiResponse = ApiResponse.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(errorMessage)
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<?> handleTokenExpiredException(TokenExpiredException ex) {
        String errorMessage = ex.getMessage();

        logger.error(errorMessage, ex);

        ApiResponse apiResponse = ApiResponse.builder()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .message(errorMessage)
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        String errorMessage = ex.getMessage();

        logger.error(errorMessage, ex);

        ApiResponse apiResponse = ApiResponse.builder()
                .statusCode(HttpStatus.CONFLICT.value())
                .message(errorMessage)
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserDeletedException.class)
    public ResponseEntity<?> handleUserDeletedException(UserDeletedException ex) {
        String errorMessage = ex.getMessage();

        logger.error(errorMessage, ex);

        ApiResponse apiResponse = ApiResponse.builder()
                .statusCode(HttpStatus.GONE.value())
                .message(errorMessage)
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.GONE);
    }

    @ExceptionHandler(UserDoesNotExistException.class)
    public ResponseEntity<?> handleUserDoesNotExistException(UserDoesNotExistException ex) {
        String errorMessage = ex.getMessage();

        logger.error(errorMessage, ex);

        ApiResponse apiResponse = ApiResponse.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(errorMessage)
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDenied(AccessDeniedException ex) {
        String errorMessage = ex.getMessage();

        logger.error(errorMessage, ex);

        ApiResponse apiResponse = ApiResponse.builder()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .message(errorMessage)
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(InvalidOtpCodeException.class)
    public ResponseEntity<?> handleInvalidOtpCodeException(InvalidOtpCodeException ex) {
        String errorMessage = ex.getMessage();
        logger.error(errorMessage, ex);
        ApiResponse apiResponse = ApiResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(errorMessage)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OtpDoesNotExistException.class)
    public ResponseEntity<?> handleOtpDoesNotExistException(OtpDoesNotExistException ex) {
        String errorMessage = ex.getMessage();
        logger.error(errorMessage, ex);
        ApiResponse apiResponse = ApiResponse.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(errorMessage)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OtpExpiredException.class)
    public ResponseEntity<?> handleOtpExpiredException(OtpExpiredException ex) {
        String errorMessage = ex.getMessage();
        logger.error(errorMessage, ex);
        ApiResponse apiResponse = ApiResponse.builder()
                .statusCode(HttpStatus.GONE.value())
                .message(errorMessage)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.GONE);
    }

    @ExceptionHandler(OtpHasBeenUsedException.class)
    public ResponseEntity<?> handleOtpHasBeenUsedException(OtpHasBeenUsedException ex) {
        String errorMessage = ex.getMessage();
        logger.error(errorMessage, ex);
        ApiResponse apiResponse = ApiResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(errorMessage)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailCannotBeSendException.class)
    public ResponseEntity<?> handleEmailCannotBeSendException(EmailCannotBeSendException ex) {
        String errorMessage = ex.getMessage();
        logger.error(errorMessage, ex);
        ApiResponse apiResponse = ApiResponse.builder()
                .statusCode(HttpStatus.SERVICE_UNAVAILABLE.value())
                .message(errorMessage)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(UserDoesNotLoginException.class)
    public ResponseEntity<?> handleUserDoesNotLoginException(UserDoesNotLoginException ex) {
        String errorMessage = ex.getMessage();
        logger.error(errorMessage, ex);
        ApiResponse apiResponse = ApiResponse.builder()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .message(errorMessage)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NotImageFileException.class)
    public ResponseEntity<?> handleNotImageFileException(NotImageFileException ex) {
        String errorMessage = ex.getMessage();
        logger.error(errorMessage, ex);
        ApiResponse apiResponse = ApiResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(errorMessage)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<?> handleProductNotFoundException(ProductNotFoundException ex) {
        String errorMessage = ex.getMessage();

        logger.error(errorMessage, ex);

        ApiResponse apiResponse = ApiResponse.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(errorMessage)
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProductDeletedException.class)
    public ResponseEntity<?> handleProductDeletedException(ProductDeletedException ex) {
        String errorMessage = ex.getMessage();
        logger.error(errorMessage, ex);
        ApiResponse apiResponse = ApiResponse.builder()
                .statusCode(HttpStatus.GONE.value())
                .message(errorMessage)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.GONE);
    }

    @ExceptionHandler(RatingDeletedException.class)
    public ResponseEntity<?> handleRatingDeletedException(RatingDeletedException ex) {
        String errorMessage = ex.getMessage();
        logger.error(errorMessage, ex);
        ApiResponse apiResponse = ApiResponse.builder()
                .statusCode(HttpStatus.GONE.value())
                .message(errorMessage)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.GONE);
    }

    @ExceptionHandler(RatingHasBeenMadeException.class)
    public ResponseEntity<?> handleRatingHasBeenMadeException(RatingHasBeenMadeException ex) {
        String errorMessage = ex.getMessage();
        logger.error(errorMessage, ex);
        ApiResponse apiResponse = ApiResponse.builder()
                .statusCode(HttpStatus.CONFLICT.value())
                .message(errorMessage)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(RatingHasBeenRestoreException.class)
    public ResponseEntity<?> handleRatingHasBeenMadeException(RatingHasBeenRestoreException ex) {
        String errorMessage = ex.getMessage();
        logger.error(errorMessage, ex);
        ApiResponse apiResponse = ApiResponse.builder()
                .statusCode(HttpStatus.CONFLICT.value())
                .message(errorMessage)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(RatingNotFoundException.class)
    public ResponseEntity<?> handleRatingNotFoundException(RatingNotFoundException ex) {
        String errorMessage = ex.getMessage();
        logger.error(errorMessage, ex);
        ApiResponse apiResponse = ApiResponse.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(errorMessage)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ApiResponse> handleCategoryNotFound(CategoryNotFoundException ex) {
        String errorMessage = ex.getMessage();
        logger.error(errorMessage, ex);
        return new ResponseEntity<>(
                ApiResponse.builder()
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .message(errorMessage)
                        .build(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(CategoryAlreadyExistsException.class)
    public ResponseEntity<ApiResponse> handleCategoryAlreadyExists(CategoryAlreadyExistsException ex) {
        logger.error(ex.getMessage(), ex);

        return new ResponseEntity<>(
                ApiResponse.builder()
                        .statusCode(HttpStatus.CONFLICT.value())
                        .message(ex.getMessage())
                        .build(),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(ProductCategoryNotFoundException.class)
    public ResponseEntity<ApiResponse> handleProductCategoryNotFound(ProductCategoryNotFoundException ex) {
        logger.error(ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.builder()
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .message(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> errors = result.getFieldErrors();
        Map<String, String> errorMap = new HashMap<>();
        for (FieldError error : errors) {
            errorMap.put(error.getField(), error.getDefaultMessage());
        }
        return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> BaseException(Exception ex) {
        String errorMessage = ex.getMessage();
        logger.error(errorMessage, ex);
        ApiResponse apiResponse = ApiResponse.builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(errorMessage)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> BaseRuntimeException(RuntimeException ex) {
        String errorMessage = ex.getMessage();
        logger.error(errorMessage, ex);
        ApiResponse apiResponse = ApiResponse.builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(errorMessage)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}