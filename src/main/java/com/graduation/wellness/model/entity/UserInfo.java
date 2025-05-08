package com.graduation.wellness.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import com.graduation.wellness.model.enums.ActivityLevel;
import com.graduation.wellness.model.enums.ExperienceLevel;
import com.graduation.wellness.model.enums.Gender;
import com.graduation.wellness.model.enums.Goal;
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
    private Long id;            // Same as user's ID

    @OneToOne
    @MapsId                     // This makes UserInfo use the User's ID
    @JoinColumn(name = "id")    // This will also be the PK and FK
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;                          //only these values: Male / Female

    @Column(name = "age")
    private int age;

    @Column(name = "weight")
    private int weight;

    @Column(name = "height")
    private int height;

    @Enumerated(EnumType.STRING)
    @Column(name = "goal", nullable = false)
    private Goal goal;                              //only these values: Weight Cut / Muscle Gain / Increase Strength

    @Enumerated(EnumType.STRING)
    @Column(name = "activity_level", nullable = false)
    private ActivityLevel activityLevel;            //only these values: sedentary / lightly active / Moderately active / very active

    @Enumerated(EnumType.STRING)
    @Column(name = "experience_level", nullable = false)
    private ExperienceLevel experienceLevel;        //only these values: Beginner / Intermediate / advanced

    @Min(2)
    @Max(6)
    @Column(name = "days_per_week")
    private int daysPerWeek;                        //only these values: 1 / 2 / 3 / 4 / 5 / 6

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