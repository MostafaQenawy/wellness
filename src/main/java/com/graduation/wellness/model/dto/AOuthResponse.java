package com.graduation.wellness.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class AOuthResponse {
    private String email;
    private String accessToken;

    public AOuthResponse(String email){
        this.email = email;
    }
}
