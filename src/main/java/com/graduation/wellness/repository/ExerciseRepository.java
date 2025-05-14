package com.graduation.wellness.repository;

import com.graduation.wellness.model.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    List<Exercise> findBySimilarGroupId(Integer similarGroupId);

    List<Exercise> findByTargetMuscle(String targetMuscle);

    List<Exercise> findByRegionMuscle(String regionMuscle);
}
