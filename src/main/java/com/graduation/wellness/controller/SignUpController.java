package com.graduation.wellness.controller;

import com.graduation.wellness.service.UserInfoService;
import com.graduation.wellness.service.UserWorkoutPlanService;
import com.graduation.wellness.service.WorkoutPlanService;
import com.graduation.wellness.model.dto.WorkoutPlanDTO;
import com.graduation.wellness.model.entity.UserInfo;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/signup")
public class SignUpController {
    private final UserInfoService userInfoService;
    private final WorkoutPlanService workoutPlanService;
    private final UserWorkoutPlanService userWorkoutPlanService;

    public SignUpController(UserInfoService userInfoService
            , WorkoutPlanService workoutPlanService
            , UserWorkoutPlanService userWorkoutPlanService) {
        this.userInfoService = userInfoService;
        this.workoutPlanService = workoutPlanService;
        this.userWorkoutPlanService = userWorkoutPlanService;
    }

    @GetMapping("/save")
    public void saveUserDataApi(@RequestBody UserInfo userInfo, @RequestParam long userID) {
        userInfoService.saveUserData(userInfo, userID);
        WorkoutPlanDTO dto = workoutPlanService.getPlanToUser(userInfo);    //Get from DB match this user
        userWorkoutPlanService.assignPlanToUser(userInfo, dto);             //Assign this plan to a user
    }
}