package org.example.wellness_hub.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.example.wellness_hub.model.enums.ActivityLevel;
import org.example.wellness_hub.model.enums.ExperienceLevel;
import org.example.wellness_hub.model.enums.Gender;
import org.example.wellness_hub.model.enums.Goal;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

import static org.example.wellness_hub.util.AgeClassifier.classifyAge;
import static org.example.wellness_hub.util.BMICalculator.BMICalculate;
import static org.example.wellness_hub.util.BMIClassifier.classifyBMI;

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