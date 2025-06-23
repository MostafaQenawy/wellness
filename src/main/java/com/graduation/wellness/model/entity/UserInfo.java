package com.graduation.wellness.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.graduation.wellness.model.enums.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

import static com.graduation.wellness.util.AgeClassifier.classifyAge;
import static com.graduation.wellness.util.BMICalculator.BMICalculate;
import static com.graduation.wellness.util.BMIClassifier.classifyBMI;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_info")
public class UserInfo {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Column(name = "age")
    private int age;

    @Column(name = "weight")
    private int weight;

    @Column(name = "height")
    private int height;

    @Enumerated(EnumType.STRING)
    @Column(name = "goal", nullable = false)
    private Goal goal;

    @Enumerated(EnumType.STRING)
    @Column(name = "activity_level", nullable = false)
    private ActivityLevel activityLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "experience_level", nullable = false)
    private ExperienceLevel experienceLevel;

    @Min(2)
    @Max(6)
    @Column(name = "days_per_week", nullable = false)
    private int daysPerWeek;

    @JsonManagedReference
    @OneToOne(mappedBy = "userInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserPlan userPlan;

    @ManyToMany
    @JoinTable(
            name = "user_favourite_exercises",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "exercise_id")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Exercise> favouriteExercises = new ArrayList<>();

    public boolean isMale() {
        return this.gender == Gender.MALE;
    }

    @Transient
    public double getBMI() {
        return BMICalculate(weight, height);
    }

    @Transient
    public String getBMICategory() {
        return classifyBMI(getBMI());
    }

    @Transient
    public String getAgeGroup() {
        return classifyAge(age);
    }
}