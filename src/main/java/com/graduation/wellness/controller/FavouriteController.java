package com.graduation.wellness.controller;

import com.graduation.wellness.model.dto.ExerciseDTO;
import com.graduation.wellness.service.ExerciseService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/favourite")
public class FavouriteController {
    private final ExerciseService exerciseService;

    @GetMapping("getFavouriteExercises")
    public List<ExerciseDTO> getFavouriteExercises(){
        return exerciseService.getFavouriteExercises();
    }
}