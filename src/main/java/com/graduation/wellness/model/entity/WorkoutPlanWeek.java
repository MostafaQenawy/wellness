package com.graduation.wellness.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "template_plan_weeks")
public class WorkoutPlanWeek {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int weekNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    private WorkoutPlan plan;

    @OneToMany(mappedBy = "planWeek", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkoutPlanWeekDay> days;
}
