package com.graduation.wellness.service;

import com.graduation.wellness.model.dto.*;
import com.graduation.wellness.model.entity.*;
import com.graduation.wellness.repository.UserPlanRep;
import com.graduation.wellness.repository.UserPlanWeekDayExerciseRep;
import com.graduation.wellness.repository.UserPlanWeekDayRep;
import com.graduation.wellness.repository.UserPlanWeekRep;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import com.graduation.wellness.model.enums.Gender;
import com.graduation.wellness.util.UserWorkoutPlanMapper;
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
        boolean isMale = userInfo.getGender() == Gender.MALE;

        String goalSets = switch (userInfo.getGoal()) {
            case WEIGHT_CUT -> "Sets: 3 - Reps 12 ~ 15";
            case INCREASE_STRENGTH -> "Sets: 3 - Reps 3 ~ 6";
            case MUSCLE_GAIN -> "Sets: 3 - Reps 8 ~ 12";
        };



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
                            .sets(goalSets)
                            .build();
                    if(isMale){
                        userExercise.setVideoURL(exerciseDTO.getExercise().getMaleVideoUrl());
                    }
                    else{
                        userExercise.setVideoURL(exerciseDTO.getExercise().getFemaleVideoUrl());
                    }

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


    public void assignDoneToExercise(long userID, long exerciseID, long dayID, long weekID) {
        Optional<UserPlanWeekDayExercise> optionalExercise =
                userPlanWeekDayExerciseRep.findByPlanWeekDay_IdAndExercise_IdAndPlanWeekDay_PlanWeek_IdAndPlanWeekDay_PlanWeek_Plan_UserInfo_Id
                        (dayID, exerciseID, weekID, userID);

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



