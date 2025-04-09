package com.example.apisample.service.Implement;


import com.example.apisample.entity.User;
import com.example.apisample.exception.jwtservice.InvalidCredentialsException;
import com.example.apisample.exception.userservice.AccountSuspendedException;
import com.example.apisample.exception.userservice.UserDeletedException;
import com.example.apisample.exception.userservice.UserDoesNotExistException;
import com.example.apisample.repository.RoleRepository;
import com.example.apisample.repository.UserRepository;
import com.example.apisample.service.Interface.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;



    private static final String DATE_PATTERN = "yyyy-MM-dd";

    public User login(String email, String password) throws InvalidCredentialsException, AccountSuspendedException, UserDeletedException {

        User optionalUser = userRepository.findByEmail(email);

        if(optionalUser == null){
            try {
                throw new UserDoesNotExistException();
            } catch (UserDoesNotExistException e) {
                throw new RuntimeException(e);
            }
        }

        if(optionalUser.getIsDeleted()){
            throw new UserDeletedException();
        }

        if(Objects.equals(optionalUser.getIsDeleted(), true)){
            throw new AccountSuspendedException();
        }

        if (!passwordEncoder.matches(password, optionalUser.getPassword())) {
            throw new InvalidCredentialsException();
        }

        return optionalUser;
    }

    public User getUserByEmail(String email) throws UserDoesNotExistException {
        User optionalUser = userRepository.findByEmail(email);

        if(optionalUser == null){
            try {
                throw new UserDoesNotExistException();
            } catch (UserDoesNotExistException e) {
                throw new RuntimeException(e);
            }
        }

        return optionalUser;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User optionalUser = userRepository.findByEmail(username);

        if(optionalUser == null){
            try {
                throw new UserDoesNotExistException();
            } catch (UserDoesNotExistException e) {
                throw new RuntimeException(e);
            }
        }

        return optionalUser;
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }
}
