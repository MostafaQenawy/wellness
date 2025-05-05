package com.graduation.wellness.repository;

import com.graduation.wellness.model.entity.UserPlanWeekDayExercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPlanWeekDayExerciseRep extends JpaRepository<UserPlanWeekDayExercise, Long> {
    Optional<UserPlanWeekDayExercise> findByPlanWeekDay_IdAndExercise_IdAndPlanWeekDay_PlanWeek_IdAndPlanWeekDay_PlanWeek_Plan_UserInfo_Id(
            Long dayId, Long exerciseId, Long weekId, Long userId);
}
