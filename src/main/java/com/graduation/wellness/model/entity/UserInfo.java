package com.graduation.wellness.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

import static com.graduation.wellness.util.AgeClassifier.classifyAge;
import static com.graduation.wellness.util.BMICalculator.BMICalculate;
import static com.graduation.wellness.util.BMIClassifier.classifyBMI;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_info")
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "gender")
    private String gender;          //only these values: Male / Female

    @Column(name = "age")
    private int age;

    @Column(name = "weight")
    private int weight;

    @Column(name = "height")
    private int height;

    @Column(name = "goal")
    private String goal;                //only these values: Weight Cut / Muscle Gain / Increase Strength

    @Column(name = "activity_level")
    private String activityLevel;       //only these values: sedentary / lightly active / Moderately active / very active

    @Column(name = "experience_level")
    private String experienceLevel;     //only these values: Beginner / Intermediate / advanced

    @Column(name = "days_per_week")
    private int daysPerWeek;            //only these values: 1 / 2 / 3 / 4 / 5 / 6

    @JsonManagedReference // Indicates the forward part of the relationship
    @OneToOne(mappedBy = "userInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserPlan UserWorkoutPlan;

    @ManyToMany
    @JoinTable(
            name = "user_favourite_exercises",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "exercise_id")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Exercise> favouriteExercises = new ArrayList<>();


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