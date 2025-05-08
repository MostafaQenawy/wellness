package com.graduation.wellness.controller;

import com.graduation.wellness.model.dto.SwapApiBodyReq;
import com.graduation.wellness.service.ExerciseService;
import com.graduation.wellness.service.UserWorkoutPlanService;
import com.graduation.wellness.model.dto.DoneApiBodyReq;
import com.graduation.wellness.model.entity.Exercise;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/exercise")
public class ExerciseController {
    private final ExerciseService exerciseService;
    private final UserWorkoutPlanService userWorkoutPlanService;

    @GetMapping("/swap")
    public List<Exercise> getSimilarExercisesApi(@RequestParam Long exerciseID) {
        return exerciseService.getSimilarExercises(exerciseID);
    }

    @PostMapping("/setSwap")
    public void setSwapExerciseApi(@RequestBody SwapApiBodyReq swapApiBodyReq) {
        userWorkoutPlanService.swapExerciseInPlan(
                swapApiBodyReq.weekID(),
                swapApiBodyReq.dayID(),
                swapApiBodyReq.oldExerciseID(),
                swapApiBodyReq.newExerciseID());
    }

    @GetMapping("/done")
    public void exerciseDoneApi(@RequestBody DoneApiBodyReq doneApiBodyReq) {
        userWorkoutPlanService.assignDoneToExercise(
                doneApiBodyReq.exerciseID(),
                doneApiBodyReq.dayID(),
                doneApiBodyReq.weekID());
    }

    @GetMapping("/addToFavourite")
    public void addExerciseToFavoriteApi(@RequestParam Long exerciseID) {
        exerciseService.addExerciseToFavourites(exerciseID);
    }

    @GetMapping("/removeFromFavourite")
    public void removeExerciseFromFavoriteApi(@RequestParam Long exerciseID) {
        exerciseService.removeExerciseFromFavourites(exerciseID);
    }
}