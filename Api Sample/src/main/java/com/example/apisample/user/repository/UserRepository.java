package com.example.apisample.user.repository;

import com.example.apisample.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);

    Optional<User> findById(Integer id);

    Page<User> findByEmailContainingAndRole_Id(String search, Integer roleId, Pageable pageable);
}
