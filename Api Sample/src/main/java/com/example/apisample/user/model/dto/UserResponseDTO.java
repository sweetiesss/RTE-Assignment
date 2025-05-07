package com.example.apisample.user.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
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
