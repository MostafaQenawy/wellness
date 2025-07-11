package com.graduation.wellness.controller;

import com.graduation.wellness.model.dto.ExerciseDTO;
import com.graduation.wellness.service.ExerciseService;
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

    @GetMapping("/muscleExercises")
    public List<ExerciseDTO> getMuscleExercisesApi(@RequestParam String regionMuscle){
        return exerciseService.getMuscleExercises(regionMuscle);
    }
}