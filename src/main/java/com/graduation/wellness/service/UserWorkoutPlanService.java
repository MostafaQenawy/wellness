package com.graduation.wellness.service;

import com.graduation.wellness.model.dto.*;
import com.graduation.wellness.model.entity.*;
import com.graduation.wellness.repository.*;
import com.graduation.wellness.security.JwtTokenUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import com.graduation.wellness.model.enums.Gender;
import com.graduation.wellness.mapper.UserWorkoutPlanMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        String goalSets = switch (userInfo.getGoal()) {
            case WEIGHT_CUT -> "Sets: 3 - Reps 12 ~ 15";
            case INCREASE_STRENGTH -> "Sets: 3 - Reps 3 ~ 6";
            case BUILD_MUSCLE -> "Sets: 3 - Reps 8 ~ 12";
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
        String jwtToken = jwtTokenUtils.getJwtToken();
        Long userID = jwtTokenUtils.getIdFromToken(jwtToken);

        UserPlan plan = userPlanRep.findByUserInfoId(userID)
                .orElseThrow(() -> new RuntimeException("Workout plan not found for user ID: " + userID));

        return UserWorkoutPlanMapper.toDTO(plan);
    }

    public Response assignDoneToExercise(long exerciseID, long dayID, long weekID) {
        String jwtToken = jwtTokenUtils.getJwtToken();
        Long userID = jwtTokenUtils.getIdFromToken(jwtToken);

        Optional<UserPlanWeekDayExercise> optionalExercise = userPlanWeekDayExerciseRep
                .findByPlanWeekDay_IdAndExercise_IdAndPlanWeekDay_PlanWeek_IdAndPlanWeekDay_PlanWeek_Plan_UserInfo_Id
                        (dayID, exerciseID, weekID, userID);

        if (optionalExercise.isPresent()) {
            UserPlanWeekDayExercise exercise = optionalExercise.get();
            exercise.setExerciseDone(true);
            userPlanWeekDayExerciseRep.save(exercise); // This may be optional if inside @Transactional
            return new Response("success" ,"Exercise labeled done successfully!");

        } else {
            throw new EntityNotFoundException("Exercise not found for this user, day, and exercise ID");
        }
    }

    public Response swapExerciseInPlan(Long weekId, Long dayId, Long oldExerciseId, Long newExerciseId) {
        String jwtToken = jwtTokenUtils.getJwtToken();
        Long userID = jwtTokenUtils.getIdFromToken(jwtToken);

        UserPlanWeekDayExercise userExercise;
        Optional<UserPlanWeekDayExercise> optionalUserExercise = userPlanWeekDayExerciseRep
                .findByPlanWeekDay_IdAndExercise_IdAndPlanWeekDay_PlanWeek_IdAndPlanWeekDay_PlanWeek_Plan_UserInfo_Id(
                        dayId, oldExerciseId, weekId, userID);

        if (optionalUserExercise.isPresent()) {
            userExercise = optionalUserExercise.get();
            Exercise newExercise = exerciseRepository.findById(newExerciseId)
                    .orElseThrow(() -> new IllegalArgumentException("New exercise not found"));
            userExercise.setExercise(newExercise);
            userPlanWeekDayExerciseRep.save(userExercise);
            return new Response("success" ,"Exercise swapped successfully!");
        } else throw new IllegalArgumentException("Exercise not found in user's plan");
    }
}