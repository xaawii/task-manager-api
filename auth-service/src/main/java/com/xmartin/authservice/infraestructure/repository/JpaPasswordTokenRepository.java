package com.xmartin.authservice.infraestructure.repository;

import com.xmartin.authservice.infraestructure.entity.PasswordTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface JpaPasswordTokenRepository extends JpaRepository<PasswordTokenEntity, Long> {
    Optional<PasswordTokenEntity> findByToken(String token);

    void deleteByExpiryDateBefore(LocalDateTime now);
}
