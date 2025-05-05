package com.graduation.wellness.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import com.graduation.wellness.model.entity.Exercise;
import com.graduation.wellness.model.entity.UserInfo;
import com.graduation.wellness.repository.ExerciseRepository;
import com.graduation.wellness.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final UserInfoRepository userInfoRepository;

    @Autowired
    public ExerciseService(ExerciseRepository exerciseRepository, UserInfoRepository userInfoRepository) {
        this.exerciseRepository = exerciseRepository;
        this.userInfoRepository = userInfoRepository;
    }

    public List<Exercise> getSimilarExercises(long exerciseID) {
        Exercise exercise = exerciseRepository.findById(exerciseID)
                .orElseThrow(() -> new IllegalArgumentException("Exercise with ID " + exerciseID + " not found"));

        return exerciseRepository.findBySimilarGroupId(exercise.getSimilarGroupId());
    }

    public List<Exercise> getMuscleExercises(String targetMuscle) {
        return exerciseRepository.findByTargetMuscle(targetMuscle);
    }

    @Transactional
    public void addExerciseToFavourites(Long userId, Long exerciseId) {
        UserInfo user = userInfoRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new EntityNotFoundException("Exercise not found"));

        if (!user.getFavouriteExercises().contains(exercise)) {
            user.getFavouriteExercises().add(exercise);
            userInfoRepository.save(user);
        }
    }

    @Transactional
    public void removeExerciseFromFavourites(Long userId, Long exerciseId) {
        UserInfo user = userInfoRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new EntityNotFoundException("Exercise not found"));

        user.getFavouriteExercises().remove(exercise);
        userInfoRepository.save(user);
    }

    public List<Exercise> getFavouriteExercises(Long userId) {
        UserInfo user = userInfoRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return user.getFavouriteExercises();
    }
}
