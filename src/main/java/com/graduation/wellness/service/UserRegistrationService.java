package com.graduation.wellness.service;

import com.graduation.wellness.model.dto.Response;
import com.graduation.wellness.model.entity.UserInfo;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserRegistrationService {

    private final UserInfoService userInfoService;
    private final UserWorkoutPlanService userWorkoutPlanService;

    @Transactional
    public Response registerUserAndAssignPlan(UserInfo userInfo, long userID) {
        userInfoService.saveUserData(userInfo, userID);
        userWorkoutPlanService.assignPlanToUser(userInfo);
        return new Response("success" ,"User registered successfully!");
    }
}

