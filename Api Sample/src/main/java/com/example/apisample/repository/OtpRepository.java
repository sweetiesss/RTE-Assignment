package com.example.apisample.repository;

import com.example.apisample.entity.OtpCode;
import com.example.apisample.entity.User;
import com.example.apisample.enums.OtpType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface OtpRepository extends JpaRepository<OtpCode, Long> {
    Optional<OtpCode> findTopByUserAndTypeOrderByExpiresAtDesc(User user, OtpType type);
    long deleteByExpiresAtBeforeAndUsedFalse(java.time.LocalDateTime expiresAt);
}
