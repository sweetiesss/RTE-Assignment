package com.example.apisample.controller;


import com.example.apisample.model.ResponseObject;
import com.example.apisample.model.dto.message.LogMessage;
import com.example.apisample.model.dto.message.ResponseMessage;
import com.example.apisample.model.dto.user.UserRegisterRequestDTO;
import com.example.apisample.model.dto.user.UserUpdateRequestDTO;
import com.example.apisample.service.Interface.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
@Slf4j
public class UserController {
    UserService userService;

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseObject> updateUser(@PathVariable Integer id, @RequestBody UserUpdateRequestDTO updateUser) throws Exception {
        log.info(LogMessage.logStartUpdateUser);

        userService.update(id, updateUser);

        log.info(LogMessage.logSuccessUpdateUser);

        return ResponseEntity.ok(
                ResponseObject.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(ResponseMessage.msgSuccess)
                        .build()
        );
    }

}
