package com.graduation.wellness.model.dto;

import java.util.List;

public record UserPlanDTO(Long id, int daysPerWeek, List<UserPlanWeekDTO> weeks) {
}
