package com.graduation.wellness.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column( nullable = false , unique = true )
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Invalid email format")
    private String email;

    @Column( unique = true )
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Invalid email format")
    private String syncAccount;

    // Fields for OAuth2
    private String provider; // e.g., "google", "facebook"

    private String providerUserId;  // Unique ID provided by the provider (e.g., Google ID)

    @Column(nullable = false )
    @Size(min = 8 , message = "password minLength is 8")
    private String password;

    public UserDto(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
