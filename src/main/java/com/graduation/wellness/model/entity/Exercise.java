package com.graduation.wellness.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "exercises")
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // Primary Key

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "target_muscle", nullable = false)
    private String targetMuscle;

    @Column(name = "difficulty", nullable = false)
    private String difficulty;

    @Column(name = "equipment_type", nullable = false)
    private String equipment_type;

    @Column(name = "image_url", nullable = false)
    private String image_url;

    @Column(name = "male_video_url", nullable = false)
    private String maleVideoUrl;

    @Column(name = "female_video_url", nullable = false)
    private String femaleVideoUrl;

    @Column(name = "similar_group_id", nullable = false)
    private Integer similarGroupId;
}