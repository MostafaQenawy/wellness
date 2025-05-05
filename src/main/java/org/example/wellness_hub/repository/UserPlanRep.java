package org.example.wellness_hub.repository;

import org.example.wellness_hub.model.entity.UserPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPlanRep extends JpaRepository<UserPlan, Long> {
    Optional<UserPlan> findByUserInfoId(Long userId);
}
