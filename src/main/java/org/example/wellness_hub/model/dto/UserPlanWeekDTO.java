package org.example.wellness_hub.model.dto;

import java.util.List;

public record UserPlanWeekDTO(Long id, int weekNumber, List<UserPlanWeekDayDTO> days) {
}
