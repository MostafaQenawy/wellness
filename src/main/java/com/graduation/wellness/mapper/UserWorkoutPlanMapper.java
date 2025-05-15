package com.graduation.wellness.mapper;

import com.graduation.wellness.model.dto.UserPlanDTO;
import com.graduation.wellness.model.dto.UserPlanWeekDTO;
import com.graduation.wellness.model.dto.UserPlanWeekDayDTO;
import com.graduation.wellness.model.dto.UserPlanWeekDayExerciseDTO;
import com.graduation.wellness.model.entity.*;

import java.util.List;

public class UserWorkoutPlanMapper {

    public static UserPlanDTO toDTO(UserPlan plan) {
        if (plan == null) return null;

        List<UserPlanWeekDTO> dayDTOs = plan.getWeeks().stream()
                .map(UserWorkoutPlanMapper::toWeekDTO)
                .toList();
        return new UserPlanDTO(plan.getId(), plan.getDaysPerWeek(), dayDTOs);
    }

    private static UserPlanWeekDTO toWeekDTO(UserPlanWeek week) {
        List<UserPlanWeekDayDTO> dayDTOs = week.getDays().stream()
                .map(UserWorkoutPlanMapper::toDayDTO)
                .toList();
        return new UserPlanWeekDTO(week.getId(), week.getWeekNumber(), dayDTOs);
    }

    private static UserPlanWeekDayDTO toDayDTO(UserPlanWeekDay day) {
        List<UserPlanWeekDayExerciseDTO> exerciseDTOs = day.getExercises().stream()
                .map(UserWorkoutPlanMapper::toExerciseDTO)
                .toList();
        return new UserPlanWeekDayDTO(day.getId(), day.getDayNumber(), exerciseDTOs);
    }

    private static UserPlanWeekDayExerciseDTO toExerciseDTO(UserPlanWeekDayExercise entity) {
        Exercise ex = entity.getExercise();
        return new UserPlanWeekDayExerciseDTO(
                ex.getId(),
                ex.getName(),
                ex.getDescription(),
                ex.getRegionMuscle(),
                ex.getTargetMuscle(),
                ex.getDifficulty(),
                entity.getExerciseOrder(),
                entity.isExerciseDone(),
                ex.getEquipmentType(),
                entity.getSets(),
                ex.getImageUrl(),
                entity.getVideoURL()
        );
    }
}

