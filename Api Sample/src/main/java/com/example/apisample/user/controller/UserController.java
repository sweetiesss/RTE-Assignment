package com.example.apisample.user.controller;

import com.example.apisample.user.model.dto.UserResponseDTO;
import com.example.apisample.user.model.dto.UserUpdateRequestDTO;
import com.example.apisample.user.model.mapper.UserMapper;
import com.example.apisample.user.service.UserService;
import com.example.apisample.utils.ApiResponse;
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
                                                           @RequestParam(defaultValue = "id", name="sort") String sort) {
        return userService.getALlUser(pageNo,pageSize,search,sort);
    }

    @GetMapping("/admin/{id}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Integer id) {
        log.debug(LogMessage.USER_GET_BY_ID_START);

        UserResponseDTO user = userService.getUserById(id);

        log.info(LogMessage.USER_GET_BY_ID_SUCCESS);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(ResponseMessage.msgSuccess)
                        .data(user)
                        .build()
        );
    }

    @GetMapping("/admin/get-email")
    public ResponseEntity<ApiResponse> getUserByEmail(@RequestBody String email) {
        log.debug(LogMessage.USER_GET_BY_EMAIL_START);

        UserResponseDTO user = UserMapper.userToDTO(userService.getUserByEmail(email));

        log.info(LogMessage.USER_GET_BY_EMAIL_SUCCESS);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(ResponseMessage.msgSuccess)
                        .data(user)
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable Integer id, @RequestBody @Valid UserUpdateRequestDTO updateUser) {
        log.debug(LogMessage.USER_UPDATE_START);

        userService.update(id, updateUser);

        log.info(LogMessage.USER_UPDATE_SUCCESS);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(ResponseMessage.msgSuccess)
                        .build()
        );
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<ApiResponse> deleteUSer(@PathVariable Integer id) {
        log.debug(LogMessage.USER_DELETE_START);

        userService.deleteUser(id);

        log.info(LogMessage.USER_DELETE_SUCCESS);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(ResponseMessage.msgSuccess)
                        .build()
        );
    }

    @PostMapping("/admin/restore/{id}")
    public ResponseEntity<ApiResponse> restoreUSer(@PathVariable Integer id) {
        log.debug(LogMessage.USER_RESTORE_START);

        userService.restoreUser(id);

        log.info(LogMessage.USER_RESTORE_SUCCESS);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(ResponseMessage.msgSuccess)
                        .build()
        );
    }
}
