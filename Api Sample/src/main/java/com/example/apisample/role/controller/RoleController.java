package com.example.apisample.role.controller;

import com.example.apisample.role.entity.Role;
import com.example.apisample.role.model.dto.AssignRoleRequestDTO;
import com.example.apisample.role.service.RoleService;
import com.example.apisample.utils.ApiResponse;
import com.example.apisample.utils.message.LogMessage;
import com.example.apisample.utils.message.ResponseMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
@Slf4j
public class RoleController {
    private final RoleService roleService;

    @GetMapping("/roles")
    public ResponseEntity<ApiResponse> getAllRoles() {
        log.debug(LogMessage.ROLE_GET_ALL_START);

        List<Role> roles = roleService.getAllRole();

        log.info(LogMessage.ROLE_GET_ALL_SUCCESS);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(ResponseMessage.msgSuccess)
                        .data(roles)
                        .build()
        );
    }

    @GetMapping("/roles/{id}")
    public ResponseEntity<ApiResponse> getRoleById(@PathVariable int id) {
        log.debug(LogMessage.ROLE_GET_BY_ID_START);

        Role role = roleService.getRoleById(id);

        log.info(LogMessage.ROLE_GET_BY_ID_SUCCESS);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(ResponseMessage.msgSuccess)
                        .data(role)
                        .build()
        );
    }

    @PostMapping("/users/{id}/roles")
    public ResponseEntity<ApiResponse> assignRole(@RequestBody @Valid AssignRoleRequestDTO dto) {
        log.debug(LogMessage.ROLE_ASSIGN_START);

        roleService.assignRole(dto.getEmail(), dto.getRoleId());

        log.info(LogMessage.ROLE_ASSIGN_SUCCESS);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(ResponseMessage.msgSuccess)
                        .build()
        );
    }
}
