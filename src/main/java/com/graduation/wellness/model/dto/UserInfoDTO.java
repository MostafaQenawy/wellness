package com.graduation.wellness.model.dto;

import com.graduation.wellness.model.enums.ActivityLevel;
import com.graduation.wellness.model.enums.ExperienceLevel;
import com.graduation.wellness.model.enums.Gender;
import com.graduation.wellness.model.enums.Goal;
import lombok.Builder;

@Builder
public record UserInfoDTO(
        String firstName,
        String lastName,
        String email,
        Gender gender,
        int age,
        int weight,
        int height,
        Goal goal,
        ActivityLevel activityLevel,
        ExperienceLevel experienceLevel,
        int daysPerWeek,
        double bmi
) {
}