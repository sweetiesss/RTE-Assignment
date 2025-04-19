package com.example.apisample.model.mapper;

import com.example.apisample.entity.User;
import com.example.apisample.model.dto.user.UserResponseDTO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UserMapper {
    private static final String DELETED_STATUS = "Delete";
    private static final String NOY_DELETED_STATUS = "Not Deleted";
    public static UserResponseDTO userToDTO(User user){


        return UserResponseDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .role(user.getRole().getRoleName())
                .createOn(user.getCreateOn())
                .lastUpdateOn(user.getLastUpdateOn())
                .deleted(user.getDeleted() ? DELETED_STATUS : NOY_DELETED_STATUS)
                .build();
    }

}
