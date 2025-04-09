package com.example.apisample.model.dto.authdto;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String email;
    private String password;
}
