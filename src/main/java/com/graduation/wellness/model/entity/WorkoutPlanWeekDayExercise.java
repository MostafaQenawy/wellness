package com.graduation.wellness.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "template_plan_week_day_exercises")
public class WorkoutPlanWeekDayExercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "plan_week_day_id", nullable = false)
    private WorkoutPlanWeekDay planWeekDay;

    @Min(1)
    @ManyToOne
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    @Column(name = "exercise_order")
    private int exerciseOrder;
}