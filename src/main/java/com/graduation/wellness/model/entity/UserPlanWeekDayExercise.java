package com.graduation.wellness.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_plan_week_day_exercises")
public class UserPlanWeekDayExercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "plan_week_day_id", nullable = false)
    @JsonBackReference      // Prevent recursion into parent UserWorkoutPlanDay
    private UserPlanWeekDay planWeekDay;

    @ManyToOne(optional = false)
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    @Min(1)
    @Column(name = "exercise_order", nullable = false)
    private int exerciseOrder;

    @Column(name = "exercise_done", nullable = false)
    private boolean exerciseDone;

    @NotBlank
    @Column(name = "sets", nullable = false)
    private String setsInfo;
}
