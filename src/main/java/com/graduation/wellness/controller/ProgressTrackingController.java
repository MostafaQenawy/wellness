package com.graduation.wellness.controller;

import com.graduation.wellness.model.dto.DayProgressDTO;
import com.graduation.wellness.service.ProgressService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/progress")
public class ProgressTrackingController {

    private final ProgressService progressService;

    @GetMapping("/daily")
    public List<DayProgressDTO> getDailyProgress() {
        return progressService.getUserProgress();
    }
}