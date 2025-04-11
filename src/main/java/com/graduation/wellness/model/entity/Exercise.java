package com.graduation.wellness.model.entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity
@Table(name = "exercises")
@NoArgsConstructor
@AllArgsConstructor
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // Primary Key

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String targetMuscle;

    @Column(nullable = false)
    private String videoUrl;

    @Column(nullable = false)
    private String sets;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "planExercises"
            , joinColumns = @JoinColumn(name = "exercise_id")
            , inverseJoinColumns = @JoinColumn(name = "plan_id"))
    @OrderColumn(name = "id")
    private List<Plan> plan ;
}

