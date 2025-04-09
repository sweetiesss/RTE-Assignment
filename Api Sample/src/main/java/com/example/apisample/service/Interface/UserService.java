package com.example.apisample.service.Interface;


import com.example.apisample.entity.User;
import com.example.apisample.exception.jwtservice.InvalidCredentialsException;
import com.example.apisample.exception.userservice.*;

public interface UserService {

    User getUserByEmail(String email) throws UserDoesNotExistException;
    User login(String email, String password) throws UserAlreadyExistsException, InvalidCredentialsException, UserDoesNotExistException, AccountSuspendedException, UserDeletedException, InvalidateException;
    void saveUser(User user);

}
