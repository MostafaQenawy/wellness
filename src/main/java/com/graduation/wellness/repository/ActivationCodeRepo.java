package com.graduation.wellness.repository;

import com.graduation.wellness.model.entity.ActivationCode;
import com.graduation.wellness.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;


public interface ActivationCodeRepo extends JpaRepository<ActivationCode, Long> {
    Optional<ActivationCode>  findByUser(User user);
    void deleteByExpiryTimeBefore(LocalDateTime now);
}
