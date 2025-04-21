package com.example.apisample.auth.repository;

import com.example.apisample.auth.entity.OtpCode;
import com.example.apisample.auth.enums.OtpType;
import com.example.apisample.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<OtpCode, Long> {
    Optional<OtpCode> findTopByUserAndTypeOrderByExpiresAtDesc(User user, OtpType type);
    void deleteByExpiresAtBefore(LocalDateTime expiresAt);
}
