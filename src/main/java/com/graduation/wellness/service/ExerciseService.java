package com.graduation.wellness.service;

import com.graduation.wellness.mapper.ExerciseMapper;
import com.graduation.wellness.model.dto.ExerciseDTO;
import com.graduation.wellness.model.dto.Response;
import com.graduation.wellness.model.enums.Gender;
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

@Service
@AllArgsConstructor
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final UserInfoRepository userInfoRepository;
    private final JwtTokenUtils jwtTokenUtils;


    public List<ExerciseDTO> getSimilarExercises(long exerciseID) {
        String jwtToken = jwtTokenUtils.getJwtToken();
        Long userID = jwtTokenUtils.getIdFromToken(jwtToken);

        Exercise exercise = exerciseRepository.findById(exerciseID)
                .orElseThrow(() -> new IllegalArgumentException("Exercise with ID " + exerciseID + " not found"));

        UserInfo userInfo = userInfoRepository.findById(userID)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        boolean isMale = userInfo.getGender() == Gender.MALE;

        String goalSets = switch (userInfo.getGoal()) {
            case WEIGHT_CUT -> "Sets: 3 - Reps 12 ~ 15";
            case INCREASE_STRENGTH -> "Sets: 3 - Reps 3 ~ 6";
            case BUILD_MUSCLE -> "Sets: 3 - Reps 8 ~ 12";
        };

        List<Exercise> exercises = exerciseRepository.findBySimilarGroupId(exercise.getSimilarGroupId());

        return exercises.stream()
                .map(ex -> {
                    if (isMale) {
                        return ExerciseMapper.toExerciseDTO
                                (ex, ex.getMaleImageUrl(), ex.getMaleVideoUrl(), goalSets);
                    } else {
                        return ExerciseMapper.toExerciseDTO
                                (ex, ex.getFemaleImageUrl(), ex.getFemaleVideoUrl(), goalSets);
                    }
                })
                .toList();
    }

    public List<ExerciseDTO> getMuscleExercises(String regionMuscle) {
        String jwtToken = jwtTokenUtils.getJwtToken();
        Long userID = jwtTokenUtils.getIdFromToken(jwtToken);

        UserInfo userInfo = userInfoRepository.findById(userID)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        boolean isMale = userInfo.getGender() == Gender.MALE;

        String goalSets = switch (userInfo.getGoal()) {
            case WEIGHT_CUT -> "Sets: 3 - Reps 12 ~ 15";
            case INCREASE_STRENGTH -> "Sets: 3 - Reps 3 ~ 6";
            case BUILD_MUSCLE -> "Sets: 3 - Reps 8 ~ 12";
        };

        List<Exercise> exercises = exerciseRepository.findByRegionMuscle(regionMuscle);

        return exercises.stream()
                .map(exercise -> {
                    if (isMale) {
                        return ExerciseMapper.toExerciseDTO
                                (exercise, exercise.getMaleImageUrl(), exercise.getMaleVideoUrl(), goalSets);
                    } else {
                        return ExerciseMapper.toExerciseDTO
                                (exercise, exercise.getFemaleImageUrl(), exercise.getFemaleVideoUrl(), goalSets);
                    }
                })
                .toList();
    }

    @Transactional
    public Response addExerciseToFavourites(Long exerciseId) {
        String jwtToken = jwtTokenUtils.getJwtToken();
        Long userID = jwtTokenUtils.getIdFromToken(jwtToken);

        UserInfo user = userInfoRepository.findById(userID)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new EntityNotFoundException("Exercise not found"));

        if (!user.getFavouriteExercises().contains(exercise)) {
            user.getFavouriteExercises().add(exercise);
            userInfoRepository.save(user);
            return new Response("success", "Favourite Exercise added successfully!");
        } else {
            return new Response("Failed", "Exercise already added at favourites!");
        }
    }

    @Transactional
    public Response removeExerciseFromFavourites(Long exerciseId) {
        String jwtToken = jwtTokenUtils.getJwtToken();
        Long userID = jwtTokenUtils.getIdFromToken(jwtToken);

        UserInfo user = userInfoRepository.findById(userID)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new EntityNotFoundException("Exercise not found"));

        if (user.getFavouriteExercises().contains(exercise)) {
            user.getFavouriteExercises().remove(exercise);
            userInfoRepository.save(user);
            return new Response("success", "Favourite Exercise removed successfully!");
        } else {
            return new Response("Failed", "Exercise already not in favourites!");
        }
    }

    public List<ExerciseDTO> getFavouriteExercises() {
        String jwtToken = jwtTokenUtils.getJwtToken();
        Long userID = jwtTokenUtils.getIdFromToken(jwtToken);

        UserInfo userInfo = userInfoRepository.findById(userID)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (userInfo.getFavouriteExercises().isEmpty()) {
            return List.of();           // Return empty list instead of throwing
        }

        boolean isMale = userInfo.getGender() == Gender.MALE;

        String goalSets = switch (userInfo.getGoal()) {
            case WEIGHT_CUT -> "Sets: 3 - Reps 12 ~ 15";
            case INCREASE_STRENGTH -> "Sets: 3 - Reps 3 ~ 6";
            case BUILD_MUSCLE -> "Sets: 3 - Reps 8 ~ 12";
        };

        List<Exercise> exercises = userInfo.getFavouriteExercises();

        return exercises.stream()
                .map(exercise -> {
                    if (isMale) {
                        return ExerciseMapper.toExerciseDTO
                                (exercise, exercise.getMaleImageUrl(), exercise.getMaleVideoUrl(), goalSets);
                    } else {
                        return ExerciseMapper.toExerciseDTO
                                (exercise, exercise.getFemaleImageUrl(), exercise.getFemaleVideoUrl(), goalSets);
                    }
                })
                .toList();
    }
}
