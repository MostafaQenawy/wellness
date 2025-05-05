package org.example.wellness_hub.controller;

import org.example.wellness_hub.Service.UserWorkoutPlanService;
import org.example.wellness_hub.model.dto.UserPlanDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/workoutPlan")
public class UserWorkoutPlanController {
    private final UserWorkoutPlanService userWorkoutPlanService;

    @Autowired
    public UserWorkoutPlanController(UserWorkoutPlanService userWorkoutPlanService) {
        this.userWorkoutPlanService = userWorkoutPlanService;
    }

    @GetMapping(value = "/getUserPlan/{userID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserPlanDTO> getUserPlanApi(@PathVariable long userID) {
        UserPlanDTO userWorkoutPlan = userWorkoutPlanService.getUserWorkoutPlanByUserId(userID);
        return ResponseEntity.ok(userWorkoutPlan);
    }
}