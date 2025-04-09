package com.example.apisample.service.Interface;


import com.example.apisample.entity.Role;
import com.example.apisample.entity.User;
import com.example.apisample.exception.jwtservice.RoleDoesNotExistException;
import com.example.apisample.exception.userservice.UserDoesNotExistException;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    List<Role> getAllRole();
    Role getRoleById(Integer id) throws RoleDoesNotExistException;
    void assignRole(String email, Integer roleId) throws UserDoesNotExistException, RoleDoesNotExistException;
}
