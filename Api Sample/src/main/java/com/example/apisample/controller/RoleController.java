package com.example.apisample.controller;

import com.example.apisample.entity.Role;
import com.example.apisample.exception.jwtservice.RoleDoesNotExistException;
import com.example.apisample.model.ResponseObject;
import com.example.apisample.model.dto.message.LogMessage;
import com.example.apisample.model.dto.message.ResponseMessage;
import com.example.apisample.model.dto.role.AssignRoleRequestDTO;
import com.example.apisample.service.Interface.RoleService;
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
     public ResponseEntity<ResponseObject> getRoleById(@PathVariable int id) throws RoleDoesNotExistException {
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
     public ResponseEntity<ResponseObject> assignRole(@RequestBody AssignRoleRequestDTO dto) throws Exception {
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
