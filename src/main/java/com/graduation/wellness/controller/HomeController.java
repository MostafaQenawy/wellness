package com.graduation.wellness.controller;


import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/Hello")
    public String home(@AuthenticationPrincipal OAuth2User principal) {
        Map<String, Object> user = principal.getAttributes();
        return "Welcome user" + user.get("name") + "!";
    }
}
