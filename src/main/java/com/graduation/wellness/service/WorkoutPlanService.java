package com.graduation.wellness.service;

import com.graduation.wellness.mapper.WorkoutPlanMapper;
import com.graduation.wellness.model.entity.*;
import lombok.extern.slf4j.Slf4j;
import com.graduation.wellness.model.dto.WorkoutPlanDTO;
import com.graduation.wellness.repository.WorkoutPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
public class WorkoutPlanService {

    private final WorkoutPlanRepository workoutPlanRepository;

    @Autowired
    public WorkoutPlanService(WorkoutPlanRepository workoutPlanRepository) {
        this.workoutPlanRepository = workoutPlanRepository;
    }

    public WorkoutPlanDTO getPlanToUser(UserInfo userInfo) {
        return findBestTemplate(userInfo)
                .map(WorkoutPlanMapper::toPlanDto)
                .orElseThrow(() -> new NoSuchElementException("No workout plan match this user"));
    }

    private Optional<WorkoutPlan> findBestTemplate(UserInfo userInfo) {
        return switch (userInfo.getGoal()) {
            case WEIGHT_CUT, BUILD_MUSCLE -> workoutPlanRepository.findByAllAttributesIgnoreCase(
                    userInfo.getGender().name(),
                    "WEIGHT_CUT",
                    userInfo.getDaysPerWeek());
            case INCREASE_STRENGTH -> workoutPlanRepository.findByAllAttributesIgnoreCase(
                    userInfo.getGender().name(),
                    "INCREASE_STRENGTH",
                    userInfo.getDaysPerWeek());
        };
    }
}