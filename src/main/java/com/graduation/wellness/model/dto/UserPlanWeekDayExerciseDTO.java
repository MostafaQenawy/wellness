package com.graduation.wellness.model.dto;

public record UserPlanWeekDayExerciseDTO(Long id, String exerciseName, String description, String targetMuscle
        , int exerciseOrder, boolean exerciseDone, String sets, String imageUrl, String videoUrl) {
}
