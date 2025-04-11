package com.graduation.wellness.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class ActivationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code; // 6-digit activation code
    private LocalDateTime expiryTime; // Expiration timestamp

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true) // Ensure one activation per user
    private User user;

    public ActivationCode() {}

    public ActivationCode(User user, String code, LocalDateTime expiryTime) {
        this.user = user;
        this.code = code;
        this.expiryTime = expiryTime;
    }

}

