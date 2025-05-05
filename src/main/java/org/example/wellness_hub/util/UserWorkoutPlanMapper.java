package org.example.wellness_hub.util;

import org.example.wellness_hub.model.dto.UserPlanDTO;
import org.example.wellness_hub.model.dto.UserPlanWeekDTO;
import org.example.wellness_hub.model.dto.UserPlanWeekDayDTO;
import org.example.wellness_hub.model.dto.UserPlanWeekDayExerciseDTO;
import org.example.wellness_hub.model.entity.*;

import java.util.List;

public class UserWorkoutPlanMapper {

    public static UserPlanDTO toDTO(UserPlan plan) {
        if (plan == null) return null;

        List<UserPlanWeekDTO> weekDTOs = plan.getWeeks().stream()
                .map(UserWorkoutPlanMapper::toWeekDTO)
                .toList();

        return new UserPlanDTO(plan.getId(), plan.getDaysPerWeek(), weekDTOs);
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
                ex.getTargetMuscle(),
                entity.getExerciseOrder(),
                entity.isExerciseDone(),
                entity.getSets(),
                ex.getImage_url(),
                entity.getVideoURL()
        );
    }
}

