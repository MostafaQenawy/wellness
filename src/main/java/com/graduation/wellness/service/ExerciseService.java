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
import java.util.Optional;

@Service
@AllArgsConstructor
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final UserInfoRepository userInfoRepository;
    private final JwtTokenUtils jwtTokenUtils;


    public List<Exercise> getSimilarExercises(long exerciseID) {
        Exercise exercise = exerciseRepository.findById(exerciseID)
                .orElseThrow(() -> new IllegalArgumentException("Exercise with ID " + exerciseID + " not found"));

        return exerciseRepository.findBySimilarGroupId(exercise.getSimilarGroupId());
    }

    public List<ExerciseDTO> getMuscleExercises(String regionMuscle) {
        String jwtToken = jwtTokenUtils.getJwtToken();
        Long userID = jwtTokenUtils.getIdFromToken(jwtToken);

        Optional<UserInfo> userInfo = userInfoRepository.findById(userID);

        boolean isMale = userInfo.get().getGender() == Gender.MALE;

        String goalSets = switch (userInfo.get().getGoal()) {
            case WEIGHT_CUT -> "Sets: 3 - Reps 12 ~ 15";
            case INCREASE_STRENGTH -> "Sets: 3 - Reps 3 ~ 6";
            case BUILD_MUSCLE -> "Sets: 3 - Reps 8 ~ 12";
        };

        List<Exercise> exercises = exerciseRepository.findByRegionMuscle(regionMuscle);

        return exercises.stream()
                .map(exercise -> {
                    if (isMale) {
                        return ExerciseMapper.toExerciseDTO(exercise, exercise.getMaleVideoUrl(), goalSets);
                    } else {
                        return ExerciseMapper.toExerciseDTO(exercise, exercise.getFemaleVideoUrl(), goalSets);
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
            return new Response("Failed", "Exercise already removed from favourites!");
        }
    }

    public List<Exercise> getFavouriteExercises() {
        String jwtToken = jwtTokenUtils.getJwtToken();
        Long userID = jwtTokenUtils.getIdFromToken(jwtToken);

        UserInfo user = userInfoRepository.findById(userID)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return user.getFavouriteExercises();
    }
}
