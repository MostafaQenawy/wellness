package com.graduation.wellness.model.dto;

import java.util.List;

public record UserPlanWeekDTO(Long id, int weekNumber, List<UserPlanWeekDayDTO> days) {
}
