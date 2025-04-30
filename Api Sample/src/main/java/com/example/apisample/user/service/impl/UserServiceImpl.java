package com.example.apisample.user.service.impl;


import com.example.apisample.auth.service.EmailService;
import com.example.apisample.role.entity.Role;
import com.example.apisample.role.exception.RoleDoesNotExistException;
import com.example.apisample.role.repository.RoleRepository;
import com.example.apisample.user.entity.CustomUserDetails;
import com.example.apisample.user.entity.User;
import com.example.apisample.user.exception.UserAlreadyExistsException;
import com.example.apisample.user.exception.UserDoesNotExistException;
import com.example.apisample.user.model.dto.UserRegisterRequestDTO;
import com.example.apisample.user.model.dto.UserResponseDTO;
import com.example.apisample.user.model.dto.UserUpdateRequestDTO;
import com.example.apisample.user.model.mapper.UserMapper;
import com.example.apisample.user.repository.UserRepository;
import com.example.apisample.user.service.UserService;
import com.example.apisample.utils.pagination.APIPageableResponseDTO;
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
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private static final Integer CUSTOMER_ROLE_ID = 2;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public APIPageableResponseDTO<UserResponseDTO> getALlUser(int pageNo, int pageSize, String search, String sortField) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortField).ascending());

        Page<User> page = userRepository.findByEmailContaining(pageable, search);
        Page<UserResponseDTO> userDtoPage = page.map(UserMapper::userToDTO);

        return new APIPageableResponseDTO<>(userDtoPage);
    }

    @Override
    public UserResponseDTO getUserById(Integer id) {
        User optionalUser = userRepository.findById(id).orElseThrow(UserDoesNotExistException::new);

        return UserMapper.userToDTO(optionalUser);
    }

    @Override
    public User getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);

        if(user == null){
            throw new UserDoesNotExistException();
        }

        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return new CustomUserDetails(user);
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void register(UserRegisterRequestDTO registerUser) {
        User userFetched = userRepository.findByEmail(registerUser.getEmail());

        if (userFetched != null) throw new UserAlreadyExistsException();

        Role role = roleRepository.findById(CUSTOMER_ROLE_ID).orElseThrow(RoleDoesNotExistException::new);

        String generatedPassword = generateRandomPassword();

        User user = User.builder()
                .email(registerUser.getEmail())
                .phone(registerUser.getPhone())
                .firstName(registerUser.getFirstName())
                .lastName(registerUser.getLastName())
                .role(role)
                .deleted(Boolean.FALSE)
                .tokenVersion(registerUser.getDefaultTokenVersion())
                .password(passwordEncoder.encode(generatedPassword))
                .build();

        emailService.sendPasswordEmail(registerUser.getEmail(), generatedPassword);


        userRepository.save(user);
    }

    @Override
    @Transactional
    public void update(Integer id, UserUpdateRequestDTO updateUser) {
        User user = userRepository.findById(id).orElseThrow(UserDoesNotExistException::new);

        if (updateUser.getEmail() != null) user.setEmail(updateUser.getEmail());
        if (updateUser.getFirstName() != null) user.setFirstName(updateUser.getFirstName());
        if (updateUser.getLastName() != null) user.setLastName(updateUser.getLastName());
        if (updateUser.getPhone() != null) user.setPhone(updateUser.getPhone());
        if (updateUser.getCreateOn() != null) user.setCreateOn(updateUser.getCreateOn());
        if (updateUser.getUpdateOn() != null) user.setLastUpdateOn(updateUser.getUpdateOn());

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(Integer id) {
        User user = userRepository.findById(id).orElseThrow(UserDoesNotExistException::new);

        user.setDeleted(Boolean.TRUE);
        user.setTokenVersion(user.getTokenVersion() + 1);

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void restoreUser(Integer id) {
        User user = userRepository.findById(id).orElseThrow(UserDoesNotExistException::new);

        user.setDeleted(Boolean.FALSE);

        userRepository.save(user);
    }

    private String generateRandomPassword() {
        String uppercaseChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowercaseChars = "abcdefghijklmnopqrstuvwxyz";
        String specialChars = "!@#$%^&*_+";
        String numbers = "0123456789";
        int passwordLength = 8;

        SecureRandom random = new SecureRandom();
        StringBuilder passwordBuilder = new StringBuilder();

        passwordBuilder.append(uppercaseChars.charAt(random.nextInt(uppercaseChars.length())));
        passwordBuilder.append(lowercaseChars.charAt(random.nextInt(lowercaseChars.length())));
        passwordBuilder.append(specialChars.charAt(random.nextInt(specialChars.length())));
        passwordBuilder.append(numbers.charAt(random.nextInt(numbers.length())));

        int remainingLength = passwordLength - passwordBuilder.length();
        String allChars = uppercaseChars + lowercaseChars + specialChars + numbers;
        for (int i = 0; i < remainingLength; i++) {
            passwordBuilder.append(allChars.charAt(random.nextInt(allChars.length())));
        }

        List<Character> passwordChars = new ArrayList<>();
        for (int i = 0; i < passwordBuilder.length(); i++) {
            passwordChars.add(passwordBuilder.charAt(i));
        }
        Collections.shuffle(passwordChars, random);

        StringBuilder shuffledPasswordBuilder = new StringBuilder();
        for (Character c : passwordChars) {
            shuffledPasswordBuilder.append(c);
        }

        return shuffledPasswordBuilder.toString();
    }
}
