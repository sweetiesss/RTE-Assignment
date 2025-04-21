package com.example.apisample.user.model.dto;


import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Builder
@Data
public class UserResponseDTO {
    private Integer id;
    private String email;
    private String deleted;
    private String firstName;
    private String lastName;
    private String phone;
    private Instant createOn;
    private Instant lastUpdateOn;
    private String role;
}
