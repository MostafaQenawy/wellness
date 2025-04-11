package com.graduation.wellness.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "plans")
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String bodyShape;

    @Column(nullable = false)
    private String goal;

    @Column(nullable = false)
    private String activityLevel;
}
