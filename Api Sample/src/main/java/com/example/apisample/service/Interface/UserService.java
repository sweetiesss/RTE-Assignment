package com.example.apisample.service.Interface;


import com.example.apisample.entity.User;
import com.example.apisample.exception.emailservice.EmailCannotBeSendException;
import com.example.apisample.exception.jwtservice.InvalidCredentialsException;
import com.example.apisample.exception.jwtservice.RoleDoesNotExistException;
import com.example.apisample.exception.otpservice.InvalidOtpCodeException;
import com.example.apisample.exception.otpservice.OtpDoesNotExistException;
import com.example.apisample.exception.otpservice.OtpExpiredException;
import com.example.apisample.exception.otpservice.OtpHasBeenUsedException;
import com.example.apisample.exception.userservice.*;
import com.example.apisample.model.dto.auth.ResetPasswordRequestDTO;
import com.example.apisample.model.dto.user.UserRegisterRequestDTO;

public interface UserService {

    User getUserByEmail(String email) throws UserDoesNotExistException;
    void login(String email, String password) throws UserAlreadyExistsException, InvalidCredentialsException, UserDoesNotExistException, AccountSuspendedException, UserDeletedException, InvalidateException;
    void saveUser(User user);
    void logout(User user) throws UserDoesNotLoginException;
    void resetPassword(ResetPasswordRequestDTO dto, User user) throws OtpDoesNotExistException, InvalidOtpCodeException, OtpHasBeenUsedException, OtpExpiredException;
    void register(UserRegisterRequestDTO dto) throws UserAlreadyExistsException, RoleDoesNotExistException, EmailCannotBeSendException;
}
