package com.graduation.wellness.repository;

import com.graduation.wellness.model.entity.WorkoutPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkoutPlanRepository extends JpaRepository<WorkoutPlan, Long> {
    @Query("SELECT w FROM WorkoutPlan w WHERE " +
            "LOWER(w.gender) = LOWER(:gender) AND " +
            "LOWER(w.goal) = LOWER(:goal) AND " +
            "w.daysPerWeek = :daysPerWeek")
    Optional<WorkoutPlan> findByAllAttributesIgnoreCase(@Param("gender") String gender,
                                                        @Param("goal") String goal,
                                                        @Param("daysPerWeek") int daysPerWeek);

}
