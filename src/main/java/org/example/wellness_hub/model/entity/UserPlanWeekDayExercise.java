package org.example.wellness_hub.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "user_plan_week_day_exercises")
public class UserPlanWeekDayExercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "plan_week_day_id", nullable = false)
    @JsonBackReference // Prevent recursion into parent UserWorkoutPlanDay
    private UserPlanWeekDay planWeekDay;

    @ManyToOne(optional = false)
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    @Column(name = "exercise_order", nullable = false)
    private int exerciseOrder;

    @Column(name = "exercise_done", nullable = false)
    private boolean exerciseDone;

    @Column(name = "sets", nullable = false)
    private String sets;

    @Column(name = "video_url", nullable = false)
    private String videoURL;
}
