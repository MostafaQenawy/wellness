package com.graduation.wellness.model.dto;

import lombok.Builder;
import lombok.Data;
import com.graduation.wellness.model.entity.Exercise; // assuming you already have this entity

@Data
@Builder
public class WorkoutPlanWeekDayExerciseDTO {
    private Exercise exercise; // using the entity directly
    private int exerciseOrder;
}

