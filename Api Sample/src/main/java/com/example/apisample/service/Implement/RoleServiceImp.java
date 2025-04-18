package com.example.apisample.service.Implement;

import com.example.apisample.entity.Role;
import com.example.apisample.entity.User;
import com.example.apisample.exception.jwtservice.RoleDoesNotExistException;
import com.example.apisample.exception.userservice.UserDoesNotExistException;
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

    public Role getRoleById(Integer id) throws RoleDoesNotExistException {
        Optional<Role> optionalRole = roleRepository.findById(id);

        if(optionalRole.isEmpty()){
            throw new RoleDoesNotExistException();
        }

        return optionalRole.get();
    }

    public void assignRole(String email, Integer roleId) throws UserDoesNotExistException, RoleDoesNotExistException {
        User user = userService.getUserByEmail(email);

        Optional<Role> optionalRole = roleRepository.findById(roleId);

        if(optionalRole.isEmpty()){
            throw new RoleDoesNotExistException();
        }

        user.setRole(optionalRole.get());

        userService.saveUser(user);
    }
}
