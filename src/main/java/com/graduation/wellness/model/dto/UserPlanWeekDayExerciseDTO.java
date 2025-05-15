package com.graduation.wellness.model.dto;

public record UserPlanWeekDayExerciseDTO(Long id, String exerciseName, String description, String regionMuscle
        , String targetMuscle, String difficulty, int exerciseOrder, boolean exerciseDone, String equipmentType
        , String sets, String imageUrl, String videoUrl) {
}