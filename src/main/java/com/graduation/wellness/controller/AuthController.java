package com.graduation.wellness.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.graduation.wellness.model.dto.UserDto;
import com.graduation.wellness.model.entity.User;
import com.graduation.wellness.security.JWTResponseDto;
import com.graduation.wellness.security.JwtRequestDto;
import com.graduation.wellness.security.JwtTokenUtils;
import com.graduation.wellness.service.AuthService;
import com.graduation.wellness.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@AllArgsConstructor
@RestController
@Slf4j
public class AuthController {

    private UserService userService;
    private AuthService authService;
    private JwtTokenUtils jwtTokenUtils;


    @PostMapping("/login")
    public ResponseEntity<JWTResponseDto> login (@Valid @RequestBody JwtRequestDto jwtRequest){
        return ResponseEntity.ok(authService.login(jwtRequest));
    }

    @PostMapping("/facebook/login")
    public ResponseEntity<JWTResponseDto> loginWithFacebook(@RequestBody JWTResponseDto request) {
        User fbUser = userService.getUserFromFacebook(request.getAccessToken());

        if (fbUser == null || fbUser.getEmail() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

       boolean exists=userService.isExist(fbUser.getEmail());
        if (!exists) {

            userService.save(fbUser); // Register new user

        }

        String jwt = jwtTokenUtils.generateToken(fbUser.getEmail(), fbUser.getId());
        return ResponseEntity.ok(new JWTResponseDto(jwt));
    }

    @PostMapping("google/login")
    public ResponseEntity<?> loginWithGoogle(@RequestBody JWTResponseDto request) {
        User googleUser = userService.getGoogleUser(request.getAccessToken());

        if (googleUser == null || googleUser.getEmail() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Google ID token");
        }

        boolean exists=userService.isExist(googleUser.getEmail());
        if (!exists) {
            userService.save(googleUser); // Register new user
        }
        String jwt = jwtTokenUtils.generateToken(googleUser.getEmail(), googleUser.getId());
        return ResponseEntity.ok(new JWTResponseDto(jwt));
    }

    @PostMapping("/register")
    public Map register (@Valid @RequestBody User user){

        return userService.save(user);
    }

    @GetMapping("favicon.ico")
    @ResponseBody
    public void returnNoFavicon() {
        // Returning nothing to prevent error
    }

    @GetMapping("/provider")
    public ResponseEntity<JWTResponseDto> displayToken(HttpServletRequest request) {

        String token = (String) request.getSession().getAttribute("jwtToken");
        JWTResponseDto jwtResponse = new JWTResponseDto(token);

        return ResponseEntity.ok(jwtResponse);
    }
}
