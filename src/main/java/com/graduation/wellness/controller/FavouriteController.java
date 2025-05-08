package com.graduation.wellness.controller;

import com.graduation.wellness.security.JwtTokenUtils;
import com.graduation.wellness.service.ExerciseService;
import com.graduation.wellness.model.entity.Exercise;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/favourite")
public class FavouriteController {
    private final ExerciseService exerciseService;

    @GetMapping("getFavouriteExercises")
    public List<Exercise> getFavouriteExercises(){
        return exerciseService.getFavouriteExercises();
    }
}