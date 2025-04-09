package com.example.apisample.controller;

import com.example.apisample.entity.User;
import com.example.apisample.model.ResponseObject;
import com.example.apisample.model.dto.authdto.LoginRequestDTO;
import com.example.apisample.model.dto.message.ResponseMessage;
import com.example.apisample.service.Interface.JWTService;
import com.example.apisample.service.Interface.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;
    private final JWTService jwtService;


    @PostMapping("/login")
    public ResponseEntity<ResponseObject> signIn(@RequestBody LoginRequestDTO loginUser) throws Exception{

        User user = userService.login(loginUser.getEmail(), loginUser.getPassword());

        String refreshToken = jwtService.generateRefreshToken(user);
        String accessToken = jwtService.generateAccessToken(refreshToken);

        return ResponseEntity.ok(
                ResponseObject.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(ResponseMessage.msgSuccess)
                        .token(accessToken)
                        .build()
        );
    }
}
