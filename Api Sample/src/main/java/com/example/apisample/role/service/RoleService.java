package com.example.apisample.role.service;


import com.example.apisample.role.entity.Role;

import java.util.List;

public interface RoleService {
    List<Role> getAllRole();
    Role getRoleById(Integer id);
    void assignRole(String email, Integer roleId);
}
