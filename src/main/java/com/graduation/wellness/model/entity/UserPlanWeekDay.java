package com.graduation.wellness.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name = "user_plan_week_days")
public class UserPlanWeekDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "plan_week_id", nullable = false)
    @JsonBackReference // Prevent recursion into plan again.
    private UserPlanWeek planWeek;

    @Min(1)
    @Max(7)
    @Column(name = "day_number")
    private int dayNumber;

    @OrderBy("exerciseOrder ASC")
    @JsonManagedReference // Forward reference to exercises
    @OneToMany(mappedBy = "planWeekDay", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserPlanWeekDayExercise> exercises = new ArrayList<>();
}