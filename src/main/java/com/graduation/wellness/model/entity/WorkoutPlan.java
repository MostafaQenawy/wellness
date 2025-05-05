package com.graduation.wellness.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "template_plans")
public class WorkoutPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "gender")
    private String gender;          //only these values: Male / Female

    @Column(name = "goal")
    private String goal;            //only these values: Weight Cut / Muscle Gain / Increase Strength

    @Column(name = "days_per_week")
    private int daysPerWeek;       //only these values: 1 / 2 / 3 / 4 / 5 / 6

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkoutPlanWeek> weeks;
}