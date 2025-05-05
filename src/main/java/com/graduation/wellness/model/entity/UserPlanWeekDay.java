package com.graduation.wellness.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name = "user_plan_week_days")
public class UserPlanWeekDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "plan_week_id", nullable = false)
    @JsonBackReference // Prevent recursion into plan again
    private UserPlanWeek planWeek;

    @Column(name = "day_number")
    private int dayNumber;

    @OneToMany(mappedBy = "planWeekDay", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // Forward reference to exercises
    private List<UserPlanWeekDayExercise> exercises;
}