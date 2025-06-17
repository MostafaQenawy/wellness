package com.graduation.wellness.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
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
@Table(name = "user_plan_weeks")
public class UserPlanWeek {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "plan_id", nullable = false)
    @JsonBackReference
    private UserPlan plan;

    @Min(1)
    @Column(name = "week_number", nullable = false)
    private int weekNumber;

    @OrderBy("dayNumber ASC")
    @JsonManagedReference
    @OneToMany(mappedBy = "planWeek", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserPlanWeekDay> days = new ArrayList<>();
}