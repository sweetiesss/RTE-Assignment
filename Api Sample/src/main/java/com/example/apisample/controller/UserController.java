package com.example.apisample.controller;


import com.example.apisample.entity.User;
import com.example.apisample.model.ResponseObject;
import com.example.apisample.model.dto.message.LogMessage;
import com.example.apisample.model.dto.message.ResponseMessage;
import com.example.apisample.model.dto.pagination.APIPageableResponseDTO;
import com.example.apisample.model.dto.user.UserResponseDTO;
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
    private final UserService userService;

    final String DEFAULT_PAGE = "0";
    final String DEFAULT_PAGE_SIZE = "8";

    @GetMapping("/admin/get-all")
    public APIPageableResponseDTO<UserResponseDTO> getUser(@RequestParam(defaultValue = DEFAULT_PAGE, name = "page") Integer pageNo,
                                                           @RequestParam(defaultValue = DEFAULT_PAGE_SIZE, name = "size") Integer pageSize,
                                                           @RequestParam(defaultValue = "", name = "search") String search,
                                                           @RequestParam(defaultValue = "", name="sort") String sort) {
        return userService.getALlUser(pageNo,pageSize,search,sort);
    }

    @GetMapping("/admin/get/{id}")
    public ResponseEntity<ResponseObject> getUserById(@PathVariable Integer id) throws Exception {
        log.info(LogMessage.logStartGetUserById);

        UserResponseDTO user = userService.getUserById(id);

        log.info(LogMessage.logSuccessGetUserById);

        return ResponseEntity.ok(
                ResponseObject.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(ResponseMessage.msgSuccess)
                        .token(user)
                        .build()
        );
    }

    @GetMapping("/admin/get-email")
    public ResponseEntity<ResponseObject> getUserByEmail(@RequestBody String email) throws Exception {
        log.info(LogMessage.logStartGetUserByEmail);

        UserResponseDTO user = userService.getUserByEmail(email);

        log.info(LogMessage.logSuccessGetUserByEmail);

        return ResponseEntity.ok(
                ResponseObject.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(ResponseMessage.msgSuccess)
                        .token(user)
                        .build()
        );
    }

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

    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<ResponseObject> deleteUSer(@PathVariable Integer id) throws Exception {
        log.info(LogMessage.getLogStartDeleteUser);

        userService.deleteUser(id);

        log.info(LogMessage.logSuccessDeleteUser);

        return ResponseEntity.ok(
                ResponseObject.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(ResponseMessage.msgSuccess)
                        .build()
        );
    }

    @PostMapping("/admin/restore/{id}")
    public ResponseEntity<ResponseObject> restoreUSer(@PathVariable Integer id) throws Exception {
        log.info(LogMessage.logStartRestoreUser);

        userService.restoreUser(id);

        log.info(LogMessage.logSuccessRestoreUser);

        return ResponseEntity.ok(
                ResponseObject.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(ResponseMessage.msgSuccess)
                        .build()
        );
    }
}
