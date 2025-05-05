package com.graduation.wellness.model.dto;

import java.util.List;

public record UserPlanWeekDayDTO(Long id, int dayNumber, List<UserPlanWeekDayExerciseDTO> exercises) {
}
