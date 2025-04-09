package com.example.apisample.service.Interface;


import com.example.apisample.entity.Role;
import com.example.apisample.entity.User;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    List<Role> getAllRole();
     Optional<Role> getRoleById(Integer id);
    void assignRole(User user, Role role);
}
