package com.example.apisample.repository;

import com.example.apisample.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer>{

    Optional<Role> findById(Integer integer);
}
