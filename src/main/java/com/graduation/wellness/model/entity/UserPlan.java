package com.graduation.wellness.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_plans")
public class UserPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "days_per_week", nullable = false)
    private int daysPerWeek;

    @JsonBackReference // This prevents serialization of the back-reference to avoid recursion
    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserInfo userInfo;

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // This is the forward side of the relationship
    private List<UserPlanWeek> weeks = new ArrayList<>();
}
