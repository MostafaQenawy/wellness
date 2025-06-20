package com.graduation.wellness.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class WorkoutPlanWeekDTO {
    private int weekNumber;
    private List<WorkoutPlanWeekDayDTO> days;
}
