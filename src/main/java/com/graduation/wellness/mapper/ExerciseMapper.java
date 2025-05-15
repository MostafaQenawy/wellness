package com.graduation.wellness.mapper;

import com.graduation.wellness.model.dto.ExerciseDTO;
import com.graduation.wellness.model.entity.Exercise;

public class ExerciseMapper {

    public static ExerciseDTO toExerciseDTO(Exercise exercise, String videoUrl, String sets) {

        return new ExerciseDTO(
                exercise.getId(),
                exercise.getName(),
                exercise.getDescription(),
                exercise.getRegionMuscle(),
                exercise.getTargetMuscle(),
                exercise.getEquipmentType(),
                exercise.getDifficulty(),
                sets,
                exercise.getImageUrl(),
                videoUrl
        );
    }
}

