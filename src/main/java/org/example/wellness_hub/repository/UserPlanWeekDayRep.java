package org.example.wellness_hub.repository;

import org.example.wellness_hub.model.entity.UserPlanWeekDay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPlanWeekDayRep extends JpaRepository<UserPlanWeekDay, Long> {
}
