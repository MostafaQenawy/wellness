package com.graduation.wellness.controller;

import com.graduation.wellness.model.dto.UserExerciseDTO;
import com.graduation.wellness.model.entity.Exercise;
import com.graduation.wellness.service.UserWorkoutPlanService;
import com.graduation.wellness.model.dto.UserPlanDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/workoutPlan")
public class UserWorkoutPlanController {
    private final UserWorkoutPlanService userWorkoutPlanService;

    @GetMapping(value = "/getUserPlan", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserPlanDTO> getUserPlanApi() {
        UserPlanDTO userWorkoutPlan = userWorkoutPlanService.getUserWorkoutPlanByUserId();
        return ResponseEntity.ok(userWorkoutPlan);
    }
}