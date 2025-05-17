package com.graduation.wellness.controller;

import com.graduation.wellness.model.dto.Response;
import com.graduation.wellness.service.UserRegistrationService;
import com.graduation.wellness.model.entity.UserInfo;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/signup")
public class SignUpController {
    private final UserRegistrationService userRegistrationService;

/*    @GetMapping("/save")
    public Response saveUserDataApi(@RequestBody UserInfo userInfo, @RequestParam long userID) {
        return userRegistrationService.registerUserAndAssignPlan(userInfo, userID);
    }*/
}