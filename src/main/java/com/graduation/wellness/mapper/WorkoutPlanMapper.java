package com.graduation.wellness.mapper;

import com.graduation.wellness.model.dto.WorkoutPlanDTO;
import com.graduation.wellness.model.dto.WorkoutPlanWeekDTO;
import com.graduation.wellness.model.dto.WorkoutPlanWeekDayDTO;
import com.graduation.wellness.model.dto.WorkoutPlanWeekDayExerciseDTO;
import com.graduation.wellness.model.entity.WorkoutPlan;

import java.util.List;

public class WorkoutPlanMapper {
    public static WorkoutPlanDTO toPlanDto(WorkoutPlan plan) {
        List<WorkoutPlanWeekDTO> weekDTOs = plan.getWeeks().stream().map(week -> {
            List<WorkoutPlanWeekDayDTO> dayDTOs = week.getDays().stream().map(day -> {
                List<WorkoutPlanWeekDayExerciseDTO> exerciseDTOS = day.getExercises().stream().map(exercise ->
                        WorkoutPlanWeekDayExerciseDTO.builder()
                                .exercise(exercise.getExercise())
                                .exerciseOrder(exercise.getExerciseOrder())
                                .build()).toList();

                return WorkoutPlanWeekDayDTO.builder()
                        .dayNumber(day.getDayNumber())
                        .exercises(exerciseDTOS)
                        .build();
            }).toList();

            return WorkoutPlanWeekDTO.builder()
                    .weekNumber(week.getWeekNumber())
                    .days(dayDTOs)
                    .build();
        }).toList();

        return WorkoutPlanDTO.builder()
                .gender(plan.getGender().name())
                .goal(plan.getGoal().name())
                .daysPerWeek(plan.getDaysPerWeek())
                .weeks(weekDTOs)
                .build();
    }
}

