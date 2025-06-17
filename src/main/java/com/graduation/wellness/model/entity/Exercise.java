package com.graduation.wellness.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "exercises")
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "region_muscle", nullable = false)
    private String regionMuscle;

    @Column(name = "target_muscle", nullable = false)
    private String targetMuscle;

    @Column(name = "difficulty", nullable = false)
    private String difficulty;

    @Column(name = "equipment_type", nullable = false)
    private String equipmentType;

    @Column(name = "male_image_url", nullable = false)
    private String maleImageUrl;

    @Column(name = "female_image_url", nullable = false)
    private String femaleImageUrl;

    @Column(name = "male_video_url", nullable = false)
    private String maleVideoUrl;

    @Column(name = "female_video_url", nullable = false)
    private String femaleVideoUrl;

    @Column(name = "similar_group_id", nullable = false)
    private Integer similarGroupId;
}
