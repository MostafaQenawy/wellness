package org.example.wellness_hub.model.dto;

import lombok.Builder;
import lombok.Data;
import org.example.wellness_hub.model.entity.Exercise; // assuming you already have this entity

@Data
@Builder
public class WorkoutPlanWeekDayExerciseDTO {
    private Exercise exercise; // using the entity directly
    private int exerciseOrder;
}

