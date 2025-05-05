package org.example.wellness_hub.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class WorkoutPlanWeekDayDTO {
    private int dayNumber;
    private List<WorkoutPlanWeekDayExerciseDTO> exercises;
}
