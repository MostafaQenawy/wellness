package com.graduation.wellness.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "template_plan_week_days")
public class WorkoutPlanWeekDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "plan_week_id")
    private WorkoutPlanWeek planWeek;

    @Column(name = "day_number")
    private int dayNumber;

    @OneToMany(mappedBy = "planWeekDay", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkoutPlanWeekDayExercise> exercises;
}
