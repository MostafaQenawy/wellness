package org.example.wellness_hub.model.dto;

import java.util.List;

public record UserPlanWeekDayDTO(Long id, int dayNumber, List<UserPlanWeekDayExerciseDTO> exercises) {
}
