package com.graduation.wellness.repository;

import com.graduation.wellness.model.entity.WorkoutPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkoutPlanRepository extends JpaRepository<WorkoutPlan, Long> {
    @Query("SELECT wt FROM WorkoutPlan wt " +
            "WHERE wt.gender = :gender" +
            " AND wt.goal = :goal" +
            " AND wt.daysPerWeek = :daysPerWeek")
    Optional<WorkoutPlan> findByAllAttributes(@Param("gender") String gender,
                                              @Param("goal") String goal,
                                              @Param("daysPerWeek") int daysPerWeek);
}
