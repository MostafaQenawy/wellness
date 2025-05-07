package com.graduation.wellness.service;

import com.graduation.wellness.model.entity.*;
import lombok.extern.slf4j.Slf4j;
import com.graduation.wellness.model.dto.WorkoutPlanDTO;
import com.graduation.wellness.model.dto.WorkoutPlanWeekDTO;
import com.graduation.wellness.model.dto.WorkoutPlanWeekDayDTO;
import com.graduation.wellness.model.dto.WorkoutPlanWeekDayExerciseDTO;
import com.graduation.wellness.repository.WorkoutPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class WorkoutPlanService {

    private final WorkoutPlanRepository workoutPlanRepository;

    @Autowired
    public WorkoutPlanService(WorkoutPlanRepository workoutPlanRepository) {
        this.workoutPlanRepository = workoutPlanRepository;
    }

    public WorkoutPlanDTO getPlanToUser(UserInfo userInfo) {
        Optional<WorkoutPlan> bestTemplate = findBestTemplate(userInfo);

        if (bestTemplate.isEmpty()) {
            log.info("No best template");
            return null;
        }
        WorkoutPlan workoutPlan = bestTemplate.get();
        return mapToDto(workoutPlan);
    }

    private Optional<WorkoutPlan> findBestTemplate(UserInfo userInfo) {
        // If an exact match is found, return it
        return workoutPlanRepository.findByAllAttributesIgnoreCase(userInfo.getGender().name()
                , userInfo.getGoal().name(), userInfo.getDaysPerWeek()
        );
    }

    public WorkoutPlanDTO mapToDto(WorkoutPlan plan) {
        List<WorkoutPlanWeekDTO> weekDTOs = plan.getWeeks().stream().map(week -> {
            List<WorkoutPlanWeekDayDTO> dayDTOs = week.getDays().stream().map(day -> {
                List<WorkoutPlanWeekDayExerciseDTO> exerciseDTOS = day.getExercises().stream().map(exercise ->
                        WorkoutPlanWeekDayExerciseDTO.builder()
                                .exercise(exercise.getExercise()) // passing full entity
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