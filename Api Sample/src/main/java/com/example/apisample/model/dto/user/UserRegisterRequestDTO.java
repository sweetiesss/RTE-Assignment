package com.example.apisample.model.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class UserRegisterRequestDTO {
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    @JsonIgnore
    private String password;
    @JsonIgnore
    private Integer defaultTokenVersion = 1;

    public UserRegisterRequestDTO() {
        this.password = generateRandomPassword();
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
