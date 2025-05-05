package com.graduation.wellness.controller;

import com.graduation.wellness.service.ExerciseService;
import com.graduation.wellness.model.entity.Exercise;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favourite")
public class FavouriteController {
    private final ExerciseService exerciseService;

    public FavouriteController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @GetMapping("getFavouriteExercises/{userID}")
    public List<Exercise> getFavouriteExercises(@PathVariable long userID){
        return exerciseService.getFavouriteExercises(userID);
    }
}
