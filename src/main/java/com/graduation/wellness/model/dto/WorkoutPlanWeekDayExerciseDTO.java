package com.graduation.wellness.model.dto;

import lombok.Builder;
import lombok.Data;
import com.graduation.wellness.model.entity.Exercise;

@Data
@Builder
public class WorkoutPlanWeekDayExerciseDTO {
    private Exercise exercise; // using the entity directly
    private int exerciseOrder;
}

