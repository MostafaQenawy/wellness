package com.graduation.wellness.service;

import com.graduation.wellness.security.JwtTokenUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import com.graduation.wellness.model.entity.Exercise;
import com.graduation.wellness.model.entity.UserInfo;
import com.graduation.wellness.repository.ExerciseRepository;
import com.graduation.wellness.repository.UserInfoRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<Exercise> getMuscleExercises(String targetMuscle) {
        return exerciseRepository.findByTargetMuscle(targetMuscle);
    }

    @Transactional
    public void addExerciseToFavourites(Long exerciseId) {
        String jwtToken = jwtTokenUtils.getJwtToken();
        Long userID = jwtTokenUtils.getIdFromToken(jwtToken);

        UserInfo user = userInfoRepository.findById(userID)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new EntityNotFoundException("Exercise not found"));

        if (!user.getFavouriteExercises().contains(exercise)) {
            user.getFavouriteExercises().add(exercise);
            userInfoRepository.save(user);
        }
    }

    @Transactional
    public void removeExerciseFromFavourites(Long exerciseId) {
        String jwtToken = jwtTokenUtils.getJwtToken();
        Long userID = jwtTokenUtils.getIdFromToken(jwtToken);

        UserInfo user = userInfoRepository.findById(userID)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new EntityNotFoundException("Exercise not found"));

        user.getFavouriteExercises().remove(exercise);
        userInfoRepository.save(user);
    }

    public List<Exercise> getFavouriteExercises() {
        String jwtToken = jwtTokenUtils.getJwtToken();
        Long userID = jwtTokenUtils.getIdFromToken(jwtToken);

        UserInfo user = userInfoRepository.findById(userID)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return user.getFavouriteExercises();
    }
}
