package com.example.apisample.model.dto.user;

import lombok.Data;

import java.time.Instant;

@Data
public class UserUpdateRequestDTO {
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private Instant createOn;
    private Instant updateOn;
}
