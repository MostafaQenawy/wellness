package com.graduation.wellness.controller;

import com.graduation.wellness.service.ExerciseService;
import com.graduation.wellness.model.entity.Exercise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/home")
public class HomeController {

    private final ExerciseService exerciseService;

    @Autowired
    public HomeController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @GetMapping("muscleExercises/{targetMuscle}")
    public List<Exercise> getMuscleExercisesApi(@PathVariable String targetMuscle){
        return exerciseService.getMuscleExercises(targetMuscle);
    }
}
