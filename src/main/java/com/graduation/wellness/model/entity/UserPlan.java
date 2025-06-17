package com.graduation.wellness.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_plans")
public class UserPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "days_per_week", nullable = false)
    private int daysPerWeek;

    @JsonBackReference
    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserInfo userInfo;

    @OrderBy("weekNumber ASC")
    @JsonManagedReference
    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserPlanWeek> weeks = new ArrayList<>();
}
