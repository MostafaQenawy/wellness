package com.graduation.wellness.controller;

import com.graduation.wellness.service.ExerciseService;
import com.graduation.wellness.service.UserWorkoutPlanService;
import com.graduation.wellness.model.dto.UserExerciseDayRequest;
import com.graduation.wellness.model.entity.Exercise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exercise")
public class ExerciseController {
    private final ExerciseService exerciseService;
    private final UserWorkoutPlanService userWorkoutPlanService;

    @Autowired
    public ExerciseController(ExerciseService exerciseService, UserWorkoutPlanService userWorkoutPlanService) {
        this.exerciseService = exerciseService;
        this.userWorkoutPlanService = userWorkoutPlanService;
    }

    @GetMapping("/swap{exerciseID}")
    public List<Exercise> getSimilarExercisesApi(@PathVariable long exerciseID) {
        return exerciseService.getSimilarExercises(exerciseID);
    }

/*    @GetMapping("/setSwap")
    public void setSwapExerciseApi(@RequestBody long userID,long oldExerciseID, long newExerciseID, long dayID){

    }*/

    @GetMapping("/done")
    public void exerciseDoneApi(@RequestBody UserExerciseDayRequest userExerciseDayRequest) {
        userWorkoutPlanService.assignDoneToExercise(userExerciseDayRequest.userID()
                , userExerciseDayRequest.exerciseID(), userExerciseDayRequest.dayID(), userExerciseDayRequest.weekID());
    }

    @GetMapping("/addToFavourite")
    public void addExerciseToFavoriteApi(@RequestBody UserExerciseDayRequest userExerciseDayRequest) {
        exerciseService.addExerciseToFavourites(userExerciseDayRequest.userID(), userExerciseDayRequest.exerciseID());
    }

    @GetMapping("/removeFromFavourite")
    public void removeExerciseFromFavoriteApi(@RequestBody UserExerciseDayRequest userExerciseDayRequest) {
        exerciseService.removeExerciseFromFavourites(userExerciseDayRequest.userID()
                , userExerciseDayRequest.exerciseID());
    }
}
