package com.graduation.wellness.security;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtRequestDto {
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}