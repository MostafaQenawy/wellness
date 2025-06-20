package com.graduation.wellness.model.dto;

public record UserExerciseDTO(Long id, String exerciseName, String description, String targetMuscle
        , boolean exerciseDone, String sets, String imageUrl, String videoUrl) {
}
