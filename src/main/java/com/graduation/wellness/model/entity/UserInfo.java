package com.graduation.wellness.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;


@Data
@Entity
@Table(name = "user_info")
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateOfBirth;

    @Column(nullable = false, length = 10)
    private String gender;

    @Column(nullable = false)
    private double weight;

    @Column(nullable = false)
    private double height;

    @Column(nullable = false, length = 100)
    private String goal;

    @Column(nullable = false, length = 50)
    private String activityLevel;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "userInjuries"
            , joinColumns = @JoinColumn(name = "userInfo_id")
            , inverseJoinColumns = @JoinColumn(name = "injury_id"))
    @OrderColumn(name = "id")
    private List<Injury> injuries ;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Allergy allergy;

    // Enum: A user selects one predefined dietary preference
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private DietaryPreference dietaryPreference;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "roleId")
    private Plan plan;

}



