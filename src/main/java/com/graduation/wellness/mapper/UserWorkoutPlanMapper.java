package com.graduation.wellness.mapper;

import com.graduation.wellness.model.dto.UserPlanDTO;
import com.graduation.wellness.model.dto.UserPlanWeekDTO;
import com.graduation.wellness.model.dto.UserPlanWeekDayDTO;
import com.graduation.wellness.model.dto.UserPlanWeekDayExerciseDTO;
import com.graduation.wellness.model.entity.*;
import com.graduation.wellness.model.enums.Gender;

import java.util.List;

public class UserWorkoutPlanMapper {

    public static UserPlanDTO toDTO(UserPlan plan) {
        if (plan == null) return null;

        boolean isMale = plan.getUserInfo().getGender() == Gender.MALE;

        List<UserPlanWeekDTO> dayDTOs = plan.getWeeks().stream()
                .map(week -> toWeekDTO(week, isMale))
                .toList();

        return new UserPlanDTO(plan.getId(), plan.getDaysPerWeek(), dayDTOs);
    }


    private static UserPlanWeekDTO toWeekDTO(UserPlanWeek week, boolean isMale) {
        List<UserPlanWeekDayDTO> dayDTOs = week.getDays().stream()
                .map(day -> toDayDTO(day, isMale))
                .toList();
        return new UserPlanWeekDTO(week.getId(), week.getWeekNumber(), dayDTOs);
    }


    private static UserPlanWeekDayDTO toDayDTO(UserPlanWeekDay day, boolean isMale) {
        List<UserPlanWeekDayExerciseDTO> exerciseDTOs = day.getExercises().stream()
                .map(ex -> toExerciseDTO(ex, isMale))
                .toList();
        return new UserPlanWeekDayDTO(day.getId(), day.getDayNumber(), exerciseDTOs);
    }


    private static UserPlanWeekDayExerciseDTO toExerciseDTO(UserPlanWeekDayExercise entity, boolean isMale) {
        Exercise ex = entity.getExercise();
        String exerciseImageUrl;
        String exerciseVideoUrl;

        if (isMale) {
            exerciseImageUrl = ex.getMaleImageUrl();
            exerciseVideoUrl = ex.getMaleVideoUrl();
        }
        else {
            exerciseImageUrl = ex.getFemaleImageUrl();
            exerciseVideoUrl = ex.getFemaleVideoUrl();
        }

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
                exerciseImageUrl,
                exerciseVideoUrl
        );
    }

}

