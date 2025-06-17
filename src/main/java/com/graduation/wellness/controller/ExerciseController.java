package com.graduation.wellness.controller;

import com.graduation.wellness.model.dto.ExerciseDTO;
import com.graduation.wellness.model.dto.Response;
import com.graduation.wellness.model.dto.SwapApiBodyReq;
import com.graduation.wellness.service.ExerciseService;
import com.graduation.wellness.service.UserWorkoutPlanService;
import com.graduation.wellness.model.dto.DoneApiBodyReq;
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
    public List<ExerciseDTO> getSimilarExercisesApi(@RequestParam Long exerciseID) {
        return exerciseService.getSimilarExercises(exerciseID);
    }

    @PostMapping("/setSwap")
    public Response setSwapExerciseApi(@RequestBody SwapApiBodyReq swapApiBodyReq) {
        return userWorkoutPlanService.swapExerciseInPlan(
                swapApiBodyReq.weekID(),
                swapApiBodyReq.dayID(),
                swapApiBodyReq.oldExerciseID(),
                swapApiBodyReq.newExerciseID());
    }

    @GetMapping("/done")
    public Response exerciseDoneApi(@RequestBody DoneApiBodyReq doneApiBodyReq) {
        return userWorkoutPlanService.markExerciseAsDone(
                doneApiBodyReq.exerciseID(),
                doneApiBodyReq.dayID(),
                doneApiBodyReq.weekID());
    }

    @GetMapping("/addToFavourite")
    public Response addExerciseToFavoriteApi(@RequestParam Long exerciseID) {
        return exerciseService.addExerciseToFavourites(exerciseID);
    }

    @GetMapping("/removeFromFavourite")
    public Response removeExerciseFromFavoriteApi(@RequestParam Long exerciseID) {
        return exerciseService.removeExerciseFromFavourites(exerciseID);
    }
}