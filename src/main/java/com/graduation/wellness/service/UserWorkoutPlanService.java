package com.graduation.wellness.service;

import com.graduation.wellness.model.dto.*;
import com.graduation.wellness.model.entity.*;
import com.graduation.wellness.repository.*;
import com.graduation.wellness.security.JwtTokenUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import com.graduation.wellness.mapper.UserWorkoutPlanMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.graduation.wellness.util.GoalSetUtils.getGoalSets;

@Service
@AllArgsConstructor
public class UserWorkoutPlanService {

    private final WorkoutPlanService workoutPlanService;
    private final UserPlanRep userPlanRep;
    private final UserPlanWeekRep userPlanWeekRep;
    private final UserPlanWeekDayRep userPlanWeekDayRep;
    private final UserPlanWeekDayExerciseRep userPlanWeekDayExerciseRep;
    private final ExerciseRepository exerciseRepository;
    private final JwtTokenUtils jwtTokenUtils;

    @Transactional
    public void assignPlanToUser(UserInfo userInfo) {
        WorkoutPlanDTO templatePlan = workoutPlanService.getPlanToUser(userInfo);

        String goalSets = getGoalSets(userInfo.getGoal());

        UserPlan userPlan = UserPlan.builder()
                .daysPerWeek(templatePlan.getDaysPerWeek())
                .userInfo(userInfo)
                .build();
        userPlanRep.save(userPlan);

        List<UserPlanWeek> userWeeks = new ArrayList<>();

        for (WorkoutPlanWeekDTO weekDTO : templatePlan.getWeeks()) {
            UserPlanWeek userWeek = UserPlanWeek.builder()
                    .weekNumber(weekDTO.getWeekNumber())
                    .plan(userPlan)
                    .build();
            userPlanWeekRep.save(userWeek); // Save week first

            List<UserPlanWeekDay> userDays = new ArrayList<>();
            for (WorkoutPlanWeekDayDTO dayDTO : weekDTO.getDays()) {
                UserPlanWeekDay userDay = UserPlanWeekDay.builder()
                        .dayNumber(dayDTO.getDayNumber())
                        .planWeek(userWeek)
                        .build();
                userPlanWeekDayRep.save(userDay); // Save day first

                List<UserPlanWeekDayExercise> userExercises = new ArrayList<>();
                for (WorkoutPlanWeekDayExerciseDTO exerciseDTO : dayDTO.getExercises()) {
                    UserPlanWeekDayExercise userExercise = UserPlanWeekDayExercise.builder()
                            .planWeekDay(userDay)
                            .exercise(exerciseDTO.getExercise())
                            .exerciseOrder(exerciseDTO.getExerciseOrder())
                            .exerciseDone(false) // default to false
                            .setsInfo(goalSets)
                            .build();

                    userPlanWeekDayExerciseRep.save(userExercise);
                    userExercises.add(userExercise);
                }

                userDay.setExercises(userExercises);
                userPlanWeekDayRep.save(userDay); // Update day with exercises
                userDays.add(userDay);
            }

            userWeek.setDays(userDays);
            userPlanWeekRep.save(userWeek); // Update week with days
            userWeeks.add(userWeek);
        }

        userPlan.setWeeks(userWeeks);
        userPlanRep.save(userPlan); // Final save
    }

    public UserPlanDTO getUserWorkoutPlanByUserId() {
        Long userID = getCurrentUserId();

        UserPlan plan = userPlanRep.findByUserInfoId(userID)
                .orElseThrow(() -> new RuntimeException("Workout plan not found for user ID: " + userID));

        return UserWorkoutPlanMapper.toDTO(plan);
    }

    public Response markExerciseAsDone(long exerciseID, long dayID, long weekID) {
        Long userID = getCurrentUserId();

        Optional<UserPlanWeekDayExercise> optionalExercise = userPlanWeekDayExerciseRep
                .findByPlanWeekDay_IdAndExercise_IdAndPlanWeekDay_PlanWeek_IdAndPlanWeekDay_PlanWeek_Plan_UserInfo_Id
                        (dayID, exerciseID, weekID, userID);

        optionalExercise.ifPresentOrElse(
                exercise -> {
                    if (!exercise.isExerciseDone()) {
                        exercise.setExerciseDone(true);
                        userPlanWeekDayExerciseRep.save(exercise);
                    }
                },
                () -> {
                    throw new EntityNotFoundException("Exercise not found for this user, day, and exercise ID");
                }
        );
        return new Response("success", "Exercise labeled done successfully!");
    }

    public Response swapExerciseInPlan(Long weekId, Long dayId, Long oldExerciseId, Long newExerciseId) {
        Long userId = getCurrentUserId();

        // Step 1: Load the day (with exercises)
        UserPlanWeekDay planDay = userPlanWeekDayRep.findById(dayId)
                .orElseThrow(() -> new EntityNotFoundException("Plan day not found"));

        // Step 2: Check if new exercise already exists in the day's exercises
        boolean alreadyExists = planDay.getExercises().stream()
                .anyMatch(e -> e.getExercise().getId().equals(newExerciseId));
        if (alreadyExists) {
            throw new IllegalArgumentException("The new exercise already exists in this day.");
        }

        // Step 3: Load the exercise entry to swap
        UserPlanWeekDayExercise userExercise = userPlanWeekDayExerciseRep
                .findByPlanWeekDay_IdAndExercise_IdAndPlanWeekDay_PlanWeek_IdAndPlanWeekDay_PlanWeek_Plan_UserInfo_Id(
                        dayId, oldExerciseId, weekId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Exercise not found in user's plan"));

        // Step 4: Load new exercise and swap
        Exercise newExercise = exerciseRepository.findById(newExerciseId)
                .orElseThrow(() -> new IllegalArgumentException("New exercise not found"));

        userExercise.setExercise(newExercise);
        userPlanWeekDayExerciseRep.save(userExercise);

        return new Response("success", "Exercise swapped successfully!");
    }

    public Long getCurrentUserId() {
        return jwtTokenUtils.getIdFromToken(jwtTokenUtils.getJwtToken());
    }
}