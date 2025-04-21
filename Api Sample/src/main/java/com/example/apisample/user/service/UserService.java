package com.example.apisample.user.service;


import com.example.apisample.auth.exception.EmailCannotBeSendException;
import com.example.apisample.role.exception.RoleDoesNotExistException;
import com.example.apisample.user.entity.User;
import com.example.apisample.user.exception.UserAlreadyExistsException;
import com.example.apisample.user.exception.UserDoesNotExistException;
import com.example.apisample.user.model.dto.UserRegisterRequestDTO;
import com.example.apisample.user.model.dto.UserResponseDTO;
import com.example.apisample.user.model.dto.UserUpdateRequestDTO;
import com.example.apisample.utils.pagination.APIPageableResponseDTO;

public interface UserService {
    APIPageableResponseDTO<UserResponseDTO> getALlUser(int pageNo, int pageSize, String search, String sortField);
    UserResponseDTO getUserById(Integer id) ;
    User getUserByEmail(String email) ;
    void saveUser(User user);
    void register(UserRegisterRequestDTO dto) ;
    void update(Integer id, UserUpdateRequestDTO updateUser) ;
    void deleteUser(Integer id) ;
    void restoreUser(Integer id) ;
}
