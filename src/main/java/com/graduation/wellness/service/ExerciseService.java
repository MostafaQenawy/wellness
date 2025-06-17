package com.graduation.wellness.service;

import com.graduation.wellness.model.dto.ExerciseDTO;
import com.graduation.wellness.model.dto.Response;
import com.graduation.wellness.security.JwtTokenUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import com.graduation.wellness.model.entity.Exercise;
import com.graduation.wellness.model.entity.UserInfo;
import com.graduation.wellness.repository.ExerciseRepository;
import com.graduation.wellness.repository.UserInfoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.graduation.wellness.mapper.ExerciseMapper.toExerciseDTO;
import static com.graduation.wellness.util.GoalSetUtils.getGoalSets;


@Service
@AllArgsConstructor
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final UserInfoRepository userInfoRepository;
    private final JwtTokenUtils jwtTokenUtils;


    public List<ExerciseDTO> getSimilarExercises(long exerciseID) {
        UserInfo userInfo = getCurrentUserInfo();
        String goalSets = getGoalSets(userInfo.getGoal());

        Exercise exercise = exerciseRepository.findById(exerciseID)
                .orElseThrow(() -> new IllegalArgumentException("Exercise with ID " + exerciseID + " not found"));

        List<Exercise> exercises = exerciseRepository.findBySimilarGroupId(exercise.getSimilarGroupId());

        return exercises.stream()
                .map(ex -> toExerciseDTO(ex, userInfo.isMale(), goalSets))
                .toList();
    }

    public List<ExerciseDTO> getMuscleExercises(String regionMuscle) {
        UserInfo userInfo = getCurrentUserInfo();
        String goalSets = getGoalSets(userInfo.getGoal());

        List<Exercise> exercises = exerciseRepository.findByRegionMuscle(regionMuscle);

        return exercises.stream()
                .map(ex -> toExerciseDTO(ex, userInfo.isMale(), goalSets))
                .toList();
    }

    public List<ExerciseDTO> getFavouriteExercises() {
        UserInfo userInfo = getCurrentUserInfo();
        if (userInfo.getFavouriteExercises().isEmpty()) {
            return List.of();           // Return an empty list instead of throwing
        }

        String goalSets = getGoalSets(userInfo.getGoal());
        List<Exercise> exercises = userInfo.getFavouriteExercises();

        return exercises.stream()
                .map(ex -> toExerciseDTO(ex, userInfo.isMale(), goalSets))
                .toList();
    }

    @Transactional
    public Response addExerciseToFavourites(Long exerciseId) {
        UserInfo userInfo = getCurrentUserInfo();

        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new EntityNotFoundException("Exercise not found"));

        if (!userInfo.getFavouriteExercises().contains(exercise)) {
            userInfo.getFavouriteExercises().add(exercise);
            userInfoRepository.save(userInfo);
            return new Response("success", "Favourite Exercise added successfully!");
        } else {
            return new Response("failure", "Exercise already added at favourites!");
        }
    }

    @Transactional
    public Response removeExerciseFromFavourites(Long exerciseId) {
        UserInfo userInfo = getCurrentUserInfo();

        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new EntityNotFoundException("Exercise not found"));

        if (userInfo.getFavouriteExercises().contains(exercise)) {
            userInfo.getFavouriteExercises().remove(exercise);
            userInfoRepository.save(userInfo);
            return new Response("success", "Favourite Exercise removed successfully!");
        } else {
            return new Response("failure", "Exercise already not in favourites!");
        }
    }

    private UserInfo getCurrentUserInfo() {
        String jwtToken = jwtTokenUtils.getJwtToken();
        Long userId = jwtTokenUtils.getIdFromToken(jwtToken);
        return userInfoRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }
}
