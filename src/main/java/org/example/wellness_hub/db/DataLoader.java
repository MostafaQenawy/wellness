/*
package org.example.wellness_hub.db;

import org.example.wellness_hub.model.entity.WorkoutPlan;
import org.example.wellness_hub.repository.WorkoutTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DataLoader implements CommandLineRunner {

    private final WorkoutTemplateRepository workoutTemplateRepository;

    @Autowired
    public DataLoader(WorkoutTemplateRepository workoutTemplateRepository) {
        this.workoutTemplateRepository = workoutTemplateRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        loadWorkoutTemplate(
                "Muscle Gain", "Normal weight", "Youth", "Moderately Active", "Male", 5,
                """
                        Day 1: Upper Body (Bench Press, Pull-ups, Shoulder Press)
                        Day 2: Lower Body (Squats, Deadlifts, Leg Press)
                        Day 3: Rest or Active Recovery
                        Day 4: Chest & Triceps (Incline Bench, Dips, Tricep Extensions)
                        Day 5: Back & Biceps (Deadlifts, Lat Pulldown, Curls)
                        Day 6: Leg Day 2 (Squats, Lunges, Hamstring Curls)
                        Day 7: Rest"""
        );

        loadWorkoutTemplate(
                "Loss weight", "Overweight", "Adults", "Lightly Active", "Female", 4,
                """
                        Day 1: Cardio + Full Body Strength (Treadmill + Squats, Dumbbell Rows)
                        Day 2: Rest or Low-Intensity Cardio (Walking, Yoga)
                        Day 3: Upper Body (Push-ups, Dumbbell Press, Rows)
                        Day 4: Rest
                        Day 5: Lower Body (Lunges, Deadlifts, Calf Raises)
                        Day 6: HIIT + Core (Jump Rope, Planks, Russian Twists)
                        Day 7: Rest"""
        );

        loadWorkoutTemplate(
                "Strength", "Underweight", "Middle Aged", "very Active", "Male", 3,
                """
                        Day 1: Strength Training (Squats, Bench Press, Deadlifts)
                        Day 2: Rest or Light Yoga
                        Day 3: Cardio & Functional Training (Rowing, Kettlebell Swings)
                        Day 4: Rest
                        Day 5: Full Body Circuit (Pull-ups, Dips, Core Work)
                        Day 6-7: Rest & Recovery"""
        );

        loadWorkoutTemplate(
                "Muscle Gain", "Normal weight", "Elderly", "Sedentary", "Female", 2,
                """
                        Day 1: Light Strength Training (Bodyweight Squats, Seated Dumbbell Press)
                        Day 2: Rest or Light Walk
                        Day 3: Balance & Mobility (Tai Chi, Yoga, Resistance Bands)
                        Day 4-7: Rest & Gentle Movement (Stretching, Walks)"""
        );
    }

    private void loadWorkoutTemplate(String goal, String bmiCategory, String ageGroup, String activityLevel,
                                     String gender, int sessionFrequency, String plan) {
        Optional<WorkoutPlan> existingTemplate = workoutTemplateRepository
                .findByAllAttributes(goal, bmiCategory, ageGroup, activityLevel, gender, sessionFrequency);

        if (existingTemplate.isEmpty()) {
            WorkoutPlan template = WorkoutPlan.builder()
                    .goal(goal)
                    .activityLevel(activityLevel)
                    .sessionFrequency(sessionFrequency)
                    .gender(gender)
                    .bmiCategory(bmiCategory)
                    .ageGroup(ageGroup)
                    .plan(plan)
                    .build();
            workoutTemplateRepository.save(template);
        }
    }
}
*/
