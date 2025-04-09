package com.example.apisample.service.Implement;

import com.example.apisample.entity.Role;
import com.example.apisample.entity.User;
import com.example.apisample.repository.RoleRepository;
import com.example.apisample.service.Interface.RoleService;
import com.example.apisample.service.Interface.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImp implements RoleService {

    private final RoleRepository roleRepository;
    private final UserService userService;

    public List<Role> getAllRole(){return roleRepository.findAll();}

    public Optional<Role> getRoleById(Integer id){return roleRepository.findById(id);}

    public void assignRole(User user, Role role){
        user.setRole(role);

        userService.saveUser(user);
    }
}
