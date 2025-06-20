package com.graduation.wellness.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_plan_weeks")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPlanWeek {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "plan_id", nullable = false)
    @JsonBackReference
    private UserPlan plan;

    @Column(name = "week_number", nullable = false)
    private int weekNumber;

    @OneToMany(mappedBy = "planWeek", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<UserPlanWeekDay> days = new ArrayList<>();
}

