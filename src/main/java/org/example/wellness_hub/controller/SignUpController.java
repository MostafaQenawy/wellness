package org.example.wellness_hub.controller;

import org.example.wellness_hub.Service.UserService;
import org.example.wellness_hub.Service.UserWorkoutPlanService;
import org.example.wellness_hub.Service.WorkoutPlanService;
import org.example.wellness_hub.model.dto.WorkoutPlanDTO;
import org.example.wellness_hub.model.entity.UserInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/signup")
public class SignUpController {
    private final UserService userService;
    private final WorkoutPlanService workoutPlanService;
    private final UserWorkoutPlanService userWorkoutPlanService;

    public SignUpController(UserService userService
            , WorkoutPlanService workoutPlanService
            , UserWorkoutPlanService userWorkoutPlanService) {
        this.userService = userService;
        this.workoutPlanService = workoutPlanService;
        this.userWorkoutPlanService = userWorkoutPlanService;
    }

    @GetMapping("/save")
    public void saveUserDataApi(@RequestBody UserInfo userInfo) {
        userService.saveUserData(userInfo);
        WorkoutPlanDTO dto = workoutPlanService.getPlanToUser(userInfo);    //Get from DB match this user
        userWorkoutPlanService.assignPlanToUser(userInfo, dto);             //Assign this plan to a user
    }

    @GetMapping("/saveTest")
    public WorkoutPlanDTO saveTest(@RequestBody UserInfo userInfo) {
        userService.saveUserData(userInfo);
        return workoutPlanService.getPlanToUser(userInfo);    //Get from DB match this user
    }
}
