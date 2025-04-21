package com.example.apisample.model.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class UserRegisterRequestDTO {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name must not exceed 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name must not exceed 50 characters")
    private String lastName;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\d{10}$", message = "Phone must be a 10-digit number")
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
