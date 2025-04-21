package com.example.apisample.auth.dto;

import com.example.apisample.utils.message.ValidateMessage;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequestDTO {

    @NotBlank(message = ValidateMessage.EMAIL_NULL)
    @Email(message = ValidateMessage.INVALID_EMAIL_ADDRESS)
    private String email;

    @NotBlank(message = ValidateMessage.PASSWORD_NULL)
    @Size(min = 1, max = 100, message = "Password must be between 6 and 100 characters")
    private String password;
}
