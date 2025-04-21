package com.example.apisample.role.service.impl;

import com.example.apisample.role.entity.Role;
import com.example.apisample.role.exception.RoleDoesNotExistException;
import com.example.apisample.role.repository.RoleRepository;
import com.example.apisample.role.service.RoleService;
import com.example.apisample.user.entity.User;
import com.example.apisample.user.exception.UserDoesNotExistException;
import com.example.apisample.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final UserService userService;

    @Override
    public List<Role> getAllRole(){return roleRepository.findAll();}

    @Override
    public Role getRoleById(Integer id)  {

        return roleRepository.findById(id).orElseThrow(RoleDoesNotExistException::new);
    }

    @Override
    @Transactional
    public void assignRole(String email, Integer roleId) {
        User user = userService.getUserByEmail(email);

        Role role = roleRepository.findById(roleId).orElseThrow(RoleDoesNotExistException::new);

        user.setRole(role);

        userService.saveUser(user);
    }
}
