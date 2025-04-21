package com.example.apisample.role.repository;

import com.example.apisample.role.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer>{

    Optional<Role> findById(Integer integer);
}
