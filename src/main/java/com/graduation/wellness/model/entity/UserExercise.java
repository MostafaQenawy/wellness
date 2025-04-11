package com.graduation.wellness.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "userExercises")
@NoArgsConstructor
@AllArgsConstructor
public class UserExercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "userInfo_id", nullable = false)
    private UserInfo userInfo;

    @ManyToOne
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    @Column(nullable = false)
    private boolean status;
}
