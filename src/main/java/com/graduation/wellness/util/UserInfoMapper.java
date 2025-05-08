package com.graduation.wellness.util;

import com.graduation.wellness.model.dto.UserInfoDTO;
import com.graduation.wellness.model.entity.User;
import com.graduation.wellness.model.entity.UserInfo;

public class UserInfoMapper {
    public static UserInfoDTO toDTO(User user, UserInfo userInfo) {
        return UserInfoDTO.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .gender(userInfo.getGender())
                .age(userInfo.getAge())
                .weight(userInfo.getWeight())
                .height(userInfo.getHeight())
                .goal(userInfo.getGoal())
                .activityLevel(userInfo.getActivityLevel())
                .experienceLevel(userInfo.getExperienceLevel())
                .daysPerWeek(userInfo.getDaysPerWeek())
                .bmi(userInfo.getBMI())
                .build();
    }
}

