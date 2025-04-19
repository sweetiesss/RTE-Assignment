package com.example.apisample.model.dto.role;

import lombok.Data;

@Data
public class AssignRoleRequestDTO {
    private String email;
    private Integer roleId;
}
