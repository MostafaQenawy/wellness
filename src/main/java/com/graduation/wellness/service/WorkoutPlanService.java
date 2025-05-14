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

import java.util.List;
import java.util.NoSuchElementException;
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
        return findBestTemplate(userInfo)
                .map(this::mapToDto)
                .orElseThrow(() -> new NoSuchElementException("No workout plan match this user"));
    }


    private Optional<WorkoutPlan> findBestTemplate(UserInfo userInfo) {
        return switch (userInfo.getGoal()) {
            case WEIGHT_CUT, BUILD_MUSCLE -> workoutPlanRepository.findByAllAttributesIgnoreCase(
                    userInfo.getGender().name(),
                    "WEIGHT_CUT",
                    userInfo.getDaysPerWeek());
            case INCREASE_STRENGTH -> workoutPlanRepository.findByAllAttributesIgnoreCase(
                    userInfo.getGender().name(),
                    "INCREASE_STRENGTH",
                    userInfo.getDaysPerWeek());
        };
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