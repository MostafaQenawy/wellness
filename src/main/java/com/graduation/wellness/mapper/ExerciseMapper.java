package com.graduation.wellness.mapper;

import com.graduation.wellness.model.dto.ExerciseDTO;
import com.graduation.wellness.model.entity.Exercise;

public class ExerciseMapper {

    public static ExerciseDTO toExerciseDTO(Exercise ex, boolean isMale, String goalSets) {
        return mapToDTO(
                ex,
                isMale ? ex.getMaleImageUrl() : ex.getFemaleImageUrl(),
                isMale ? ex.getMaleVideoUrl() : ex.getFemaleVideoUrl(),
                goalSets
        );
    }

    private static ExerciseDTO mapToDTO(Exercise exercise, String imageUrl, String videoUrl, String sets) {
        return new ExerciseDTO(
                exercise.getId(),
                exercise.getName(),
                exercise.getDescription(),
                exercise.getRegionMuscle(),
                exercise.getTargetMuscle(),
                exercise.getEquipmentType(),
                exercise.getDifficulty(),
                sets,
                imageUrl,
                videoUrl
        );
    }
}

