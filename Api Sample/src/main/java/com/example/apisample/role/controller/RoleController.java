package com.example.apisample.role.controller;

import com.example.apisample.role.entity.Role;
import com.example.apisample.role.exception.RoleDoesNotExistException;
import com.example.apisample.role.model.dto.AssignRoleRequestDTO;
import com.example.apisample.role.service.RoleService;
import com.example.apisample.utils.ResponseObject;
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
@RequestMapping("/roles")
@CrossOrigin(origins = "*")
@Slf4j
public class RoleController {
     private final RoleService roleService;

     @GetMapping()
     public ResponseEntity<ResponseObject> getAllRoles() {
         log.info(LogMessage.logStartGetAllRoles);

         List<Role> roles = roleService.getAllRole();

         log.info(LogMessage.logSuccessGetAllRoles);

         return ResponseEntity.ok(
                 ResponseObject.builder()
                         .statusCode(HttpStatus.OK.value())
                         .message(ResponseMessage.msgSuccess)
                         .token(roles)
                         .build()
         );
     }

     @GetMapping("/{id}")
     public ResponseEntity<ResponseObject> getRoleById(@PathVariable int id) {
         log.info(LogMessage.logStartGetAllRolesById);

         Role role = roleService.getRoleById(id);

         log.info(LogMessage.logSuccessGetAllRolesById);

         return ResponseEntity.ok(
                 ResponseObject.builder()
                         .statusCode(HttpStatus.OK.value())
                         .message(ResponseMessage.msgSuccess)
                         .token(role)
                         .build()
         );
     }


     @PostMapping("/assign-role")
     public ResponseEntity<ResponseObject> assignRole(@RequestBody @Valid AssignRoleRequestDTO dto) {
         log.info(LogMessage.logStartAssignRole);

         roleService.assignRole(dto.getEmail(), dto.getRoleId());

         log.info(LogMessage.logSuccessAssignRole);

         return ResponseEntity.ok(
                ResponseObject.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(ResponseMessage.msgSuccess)
                        .build()
         );
    }
}
