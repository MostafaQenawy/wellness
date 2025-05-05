package com.graduation.wellness.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import com.graduation.wellness.model.dto.*;
import com.graduation.wellness.model.entity.*;
import com.graduation.wellness.repository.*;
import com.graduation.wellness.mapper.UserWorkoutPlanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserWorkoutPlanService {

    private final UserPlanRep userPlanRep;
    private final UserPlanWeekRep userPlanWeekRep;
    private final UserPlanWeekDayRep userPlanWeekDayRep;
    private final UserPlanWeekDayExerciseRep userPlanWeekDayExerciseRep;

    @Autowired
    public UserWorkoutPlanService(UserPlanRep userPlanRep
            , UserPlanWeekRep userPlanWeekRep
            , UserPlanWeekDayRep userPlanWeekDayRep
            , UserPlanWeekDayExerciseRep userPlanWeekDayExerciseRep) {
        this.userPlanRep = userPlanRep;
        this.userPlanWeekRep = userPlanWeekRep;
        this.userPlanWeekDayRep = userPlanWeekDayRep;
        this.userPlanWeekDayExerciseRep = userPlanWeekDayExerciseRep;
    }

    @Transactional
    public void assignPlanToUser(UserInfo userInfo, WorkoutPlanDTO templatePlan) {
        UserPlan userPlan = UserPlan.builder()
                .userInfo(userInfo)
                .build();
        userPlanRep.save(userPlan);

        List<UserPlanWeek> savedWeeks = new ArrayList<>();

        for (WorkoutPlanWeekDTO weekDTO : templatePlan.getWeeks()) {
            UserPlanWeek userWeek = UserPlanWeek.builder()
                    .weekNumber(weekDTO.getWeekNumber())
                    .plan(userPlan)
                    .build();
            userPlanWeekRep.save(userWeek); // Save week first

            List<UserPlanWeekDay> savedDays = new ArrayList<>();
            for (WorkoutPlanWeekDayDTO dayDTO : weekDTO.getDays()) {
                UserPlanWeekDay userDay = UserPlanWeekDay.builder()
                        .dayNumber(dayDTO.getDayNumber())
                        .planWeek(userWeek)
                        .build();
                userPlanWeekDayRep.save(userDay); // Save day first

                List<UserPlanWeekDayExercise> savedExercises = new ArrayList<>();
                for (WorkoutPlanWeekDayExerciseDTO exerciseDTO : dayDTO.getExercises()) {
                    UserPlanWeekDayExercise userExercise = UserPlanWeekDayExercise.builder()
                            .planWeekDay(userDay)
                            .exercise(exerciseDTO.getExercise())
                            .exerciseOrder(exerciseDTO.getExerciseOrder())
                            .exerciseDone(false) // default to false
                            .build();
                    userPlanWeekDayExerciseRep.save(userExercise);
                    savedExercises.add(userExercise);
                }

                userDay.setExercises(savedExercises);
                userPlanWeekDayRep.save(userDay); // Update day with exercises
                savedDays.add(userDay);
            }

            userWeek.setDays(savedDays);
            userPlanWeekRep.save(userWeek); // Update week with days
            savedWeeks.add(userWeek);
        }

        userPlan.setWeeks(savedWeeks);
        userPlanRep.save(userPlan); // Final save
    }


    public void assignDoneToExercise(long userID, long exerciseID, long dayID, long weekID) {
        Optional<UserPlanWeekDayExercise> optionalExercise =
                userPlanWeekDayExerciseRep.findByPlanWeekDay_IdAndExercise_IdAndPlanWeekDay_PlanWeek_IdAndPlanWeekDay_PlanWeek_Plan_UserInfo_Id
                        (dayID, exerciseID,weekID, userID);

        if (optionalExercise.isPresent()) {
            UserPlanWeekDayExercise exercise = optionalExercise.get();
            exercise.setExerciseDone(true);
            userPlanWeekDayExerciseRep.save(exercise); // This may be optional if inside @Transactional
        } else {
            throw new EntityNotFoundException("Exercise not found for this user, day, and exercise ID");
        }
    }

    public UserPlanDTO getUserWorkoutPlanByUserId(long userId) {
        UserPlan plan = userPlanRep.findByUserInfoId(userId)
                .orElseThrow(() -> new RuntimeException("Workout plan not found for user ID: " + userId));

        return UserWorkoutPlanMapper.toDTO(plan);
    }

}



