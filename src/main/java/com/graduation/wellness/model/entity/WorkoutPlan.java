package com.graduation.wellness.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.graduation.wellness.model.enums.Gender;
import com.graduation.wellness.model.enums.Goal;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;                          //only these values: Male / Female

    @Enumerated(EnumType.STRING)
    @Column(name = "goal", nullable = false)
    private Goal goal;                              //only these values: Weight Cut / Muscle Gain / Increase Strength

    @Min(2)
    @Max(6)
    @Column(name = "days_per_week")
    private int daysPerWeek;                        //only these values: 1 / 2 / 3 / 4 / 5 / 6

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkoutPlanWeek> weeks;
}