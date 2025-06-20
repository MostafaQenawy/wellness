package com.graduation.wellness.model.dto;

public record ExerciseDTO(Long id, String exerciseName, String description, String regioNMuscle
        , String targetMuscle,String equipmentType, String difficulty, String sets, String imageUrl, String videoUrl) {
}