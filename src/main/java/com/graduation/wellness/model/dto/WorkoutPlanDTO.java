package com.graduation.wellness.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class WorkoutPlanDTO {
    private String gender;
    private String goal;
    private int daysPerWeek;
    private List<WorkoutPlanWeekDTO> weeks;
}