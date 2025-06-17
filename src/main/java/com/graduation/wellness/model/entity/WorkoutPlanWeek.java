package com.graduation.wellness.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "template_plan_weeks")
public class WorkoutPlanWeek {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(1)
    @Column(name = "week_number", nullable = false)
    private int weekNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "plan_id", nullable = false)
    private WorkoutPlan plan;

    @OrderBy("dayNumber ASC")
    @OneToMany(mappedBy = "planWeek", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkoutPlanWeekDay> days = new ArrayList<>();
}
