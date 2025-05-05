package org.example.wellness_hub.model.dto;

import java.util.List;

public record UserPlanDTO(Long id,int daysPerWeek, List<UserPlanWeekDTO> weeks) {
}
