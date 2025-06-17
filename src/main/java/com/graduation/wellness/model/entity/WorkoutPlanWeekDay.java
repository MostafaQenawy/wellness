package com.graduation.wellness.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "template_plan_week_days")
public class WorkoutPlanWeekDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "plan_week_id", nullable = false)
    private WorkoutPlanWeek planWeek;

    @Min(1)
    @Max(7)
    @Column(name = "day_number", nullable = false)
    private int dayNumber;

    @OrderBy("exerciseOrder ASC")
    @OneToMany(mappedBy = "planWeekDay", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkoutPlanWeekDayExercise> exercises = new ArrayList<>();
}
