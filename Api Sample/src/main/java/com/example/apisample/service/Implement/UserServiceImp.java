package com.example.apisample.service.Implement;


import com.example.apisample.entity.Role;
import com.example.apisample.entity.User;
import com.example.apisample.enums.OtpType;
import com.example.apisample.exception.emailservice.EmailCannotBeSendException;
import com.example.apisample.exception.jwtservice.InvalidCredentialsException;
import com.example.apisample.exception.jwtservice.RoleDoesNotExistException;
import com.example.apisample.exception.otpservice.InvalidOtpCodeException;
import com.example.apisample.exception.otpservice.OtpDoesNotExistException;
import com.example.apisample.exception.otpservice.OtpExpiredException;
import com.example.apisample.exception.otpservice.OtpHasBeenUsedException;
import com.example.apisample.model.dto.auth.ResetPasswordRequestDTO;
import com.example.apisample.model.dto.pagination.APIPageableResponseDTO;
import com.example.apisample.model.dto.user.UserRegisterRequestDTO;
import com.example.apisample.model.dto.user.UserResponseDTO;
import com.example.apisample.model.dto.user.UserUpdateRequestDTO;
import com.example.apisample.exception.userservice.UserAlreadyExistsException;
import com.example.apisample.exception.userservice.UserDeletedException;
import com.example.apisample.exception.userservice.UserDoesNotExistException;
import com.example.apisample.exception.userservice.UserDoesNotLoginException;
import com.example.apisample.model.mapper.UserMapper;
import com.example.apisample.repository.RoleRepository;
import com.example.apisample.repository.UserRepository;
import com.example.apisample.service.Interface.EmailService;
import com.example.apisample.service.Interface.OtpService;
import com.example.apisample.service.Interface.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final OtpService otpService;
    private static final Integer CUSTOMER_ROLE_ID = 2;
    private final EmailService emailService;


    public APIPageableResponseDTO<UserResponseDTO> getALlUser(int pageNo, int pageSize, String search, String sortField) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        Page<User> page = userRepository.findByEmailContaining(pageable, search);
        Page<UserResponseDTO> userDtoPage = page.map(UserMapper::userToDTO);

        return new APIPageableResponseDTO<>(userDtoPage);
    }

    public void login(String email, String password) throws InvalidCredentialsException, UserDeletedException, UserDoesNotExistException {

        User user = userRepository.findByEmail(email);

        if(user == null){
                throw new UserDoesNotExistException();
        }

        if(user.getDeleted()){
            throw new UserDeletedException();
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        otpService.generateOtp(user, OtpType.LOGIN_2FA);
    }

    public UserResponseDTO getUserById(Integer id) throws UserDoesNotExistException {
        User optionalUser = userRepository.findByid(id).orElseThrow(UserDoesNotExistException::new);

        return UserMapper.userToDTO(optionalUser);
    }

    public User getUserByEmail(String email) throws UserDoesNotExistException {
        User optionalUser = userRepository.findByEmail(email);

        if(optionalUser == null){
            throw new UserDoesNotExistException();
        }

        return optionalUser;
    }

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

    public void logout(User user) throws UserDoesNotLoginException {
        if(user == null){
            throw new UserDoesNotLoginException();
        }

        user.setTokenVersion(user.getTokenVersion() + 1);
        userRepository.save(user);
    }

    public void resetPassword(ResetPasswordRequestDTO dto, User user) throws OtpDoesNotExistException, InvalidOtpCodeException, OtpHasBeenUsedException, OtpExpiredException {
        otpService.validateOtp(user, dto.getOtp(), OtpType.PASSWORD_RESET);

        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setLastUpdateOn(Instant.now());
        user.setTokenVersion(user.getTokenVersion() + 1);

        userRepository.save(user);
    }

    public void register(UserRegisterRequestDTO registerUser) throws UserAlreadyExistsException, RoleDoesNotExistException, EmailCannotBeSendException {
        User userFetched = userRepository.findByEmail(registerUser.getEmail());

        if (userFetched != null) throw new UserAlreadyExistsException();

        Optional<Role> optionRole = roleRepository.findById(CUSTOMER_ROLE_ID);

        if(optionRole.isEmpty()) throw new RoleDoesNotExistException();

        Role role = optionRole.get();

        User user = User.builder()
                .email(registerUser.getEmail())
                .phone(registerUser.getPhone())
                .firstName(registerUser.getFirstName())
                .lastName(registerUser.getLastName())
                .role(role)
                .deleted(Boolean.FALSE)
                .tokenVersion(registerUser.getDefaultTokenVersion())
                .password(passwordEncoder.encode(registerUser.getPassword().trim()))
                .createOn(Instant.now())
                .lastUpdateOn(Instant.now())
                .build();

        emailService.sendPasswordEmail(registerUser.getEmail(), registerUser.getPassword().trim());


        userRepository.save(user);
    }

    public void update(Integer id, UserUpdateRequestDTO updateUser) throws UserDoesNotExistException {

        Optional<User> optionalUser = userRepository.findByid(id);

        if(optionalUser.isEmpty()){
            throw new UserDoesNotExistException();
        }

        User existingUser = optionalUser.get();

        if (updateUser.getEmail() != null) existingUser.setEmail(updateUser.getEmail());
        if (updateUser.getFirstName() != null) existingUser.setFirstName(updateUser.getFirstName());
        if (updateUser.getLastName() != null) existingUser.setLastName(updateUser.getLastName());
        if (updateUser.getPhone() != null) existingUser.setPhone(updateUser.getPhone());
        if (updateUser.getCreateOn() != null) existingUser.setCreateOn(updateUser.getCreateOn());
        if (updateUser.getUpdateOn() != null) existingUser.setLastUpdateOn(updateUser.getUpdateOn());
        else existingUser.setLastUpdateOn(Instant.now());

        userRepository.save(existingUser);
    }

    public void deleteUser(Integer id) throws UserDoesNotExistException {
        Optional<User> optionalUser = userRepository.findByid(id);

        if(optionalUser.isEmpty()){
            throw new UserDoesNotExistException();
        }

        User existingUser = optionalUser.get();

        existingUser.setDeleted(Boolean.TRUE);
        existingUser.setLastUpdateOn(Instant.now());
        existingUser.setTokenVersion(existingUser.getTokenVersion() + 1);

        userRepository.save(existingUser);
    }

    public void restoreUser(Integer id) throws UserDoesNotExistException {
        Optional<User> optionalUser = userRepository.findByid(id);

        if(optionalUser.isEmpty()){
            throw new UserDoesNotExistException();
        }

        User existingUser = optionalUser.get();

        existingUser.setLastUpdateOn(Instant.now());
        existingUser.setDeleted(Boolean.FALSE);

        userRepository.save(existingUser);
    }

}
