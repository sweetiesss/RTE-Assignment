package com.example.apisample.user.controller;


import com.example.apisample.user.model.dto.UserResponseDTO;
import com.example.apisample.user.model.dto.UserUpdateRequestDTO;
import com.example.apisample.user.model.mapper.UserMapper;
import com.example.apisample.user.service.UserService;
import com.example.apisample.utils.ResponseObject;
import com.example.apisample.utils.message.LogMessage;
import com.example.apisample.utils.message.ResponseMessage;
import com.example.apisample.utils.pagination.APIPageableResponseDTO;
import jakarta.validation.Valid;
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

    @GetMapping("/admin/")
    public APIPageableResponseDTO<UserResponseDTO> getUser(@RequestParam(defaultValue = DEFAULT_PAGE, name = "page") Integer pageNo,
                                                           @RequestParam(defaultValue = DEFAULT_PAGE_SIZE, name = "size") Integer pageSize,
                                                           @RequestParam(defaultValue = "", name = "search") String search,
                                                           @RequestParam(defaultValue = "", name="sort") String sort) {
        return userService.getALlUser(pageNo,pageSize,search,sort);
    }

    @GetMapping("/admin/{id}")
    public ResponseEntity<ResponseObject> getUserById(@PathVariable Integer id) {
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
    public ResponseEntity<ResponseObject> getUserByEmail(@RequestBody String email) {
        log.info(LogMessage.logStartGetUserByEmail);

        UserResponseDTO user = UserMapper.userToDTO(userService.getUserByEmail(email));

        log.info(LogMessage.logSuccessGetUserByEmail);

        return ResponseEntity.ok(
                ResponseObject.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(ResponseMessage.msgSuccess)
                        .token(user)
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateUser(@PathVariable Integer id, @RequestBody @Valid UserUpdateRequestDTO updateUser) {
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

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<ResponseObject> deleteUSer(@PathVariable Integer id) {
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
    public ResponseEntity<ResponseObject> restoreUSer(@PathVariable Integer id) {
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
