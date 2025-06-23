package com.graduation.wellness.controller;

import com.graduation.wellness.exception.BaseApiExceptions;
import com.graduation.wellness.model.dto.AOuthResponse;
import com.graduation.wellness.model.dto.Response;
import com.graduation.wellness.model.entity.User;
import com.graduation.wellness.model.entity.UserInfo;
import com.graduation.wellness.security.JWTResponseDto;
import com.graduation.wellness.security.JwtRequestDto;
import com.graduation.wellness.security.JwtTokenUtils;
import com.graduation.wellness.service.AuthService;
import com.graduation.wellness.service.UserInfoService;
import com.graduation.wellness.service.UserRegistrationService;
import com.graduation.wellness.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@Slf4j
public class AuthController {

    private final UserService userService;
    private final AuthService authService;
    private final JwtTokenUtils jwtTokenUtils;
    private final UserInfoService userInfoService;
    private final UserRegistrationService userRegistrationService;


    @PostMapping("/login")
    public ResponseEntity<JWTResponseDto> login (@Valid @RequestBody JwtRequestDto jwtRequest){
        return ResponseEntity.ok(authService.login(jwtRequest));
    }

    @PostMapping("/facebook/login")
    public ResponseEntity<AOuthResponse> loginWithFacebook(@RequestBody JWTResponseDto request) {
        User fbUser = userService.getUserFromFacebook(request.getAccessToken());

        if (fbUser == null || fbUser.getEmail() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

       boolean exists=userService.isExist(fbUser.getEmail());
        if (!exists) {
            userService.save(fbUser); // Register new user
            return ResponseEntity.ok(new AOuthResponse(fbUser.getEmail()));
        }else {
            User user = userService.loadUserByEmail(fbUser.getEmail());
            if (!user.getProvider().equals("FACEBOOK")) {
                throw new BaseApiExceptions(
                        String.format("This Email is registered via Google", user.getEmail()),
                        HttpStatus.NOT_FOUND
                );
            }

            UserInfo userInfo = userInfoService.loadUserInfoById(user.getId());
            if(userInfo == null) {
                return ResponseEntity.ok(new AOuthResponse(user.getEmail()));
            }

            String jwt = jwtTokenUtils.generateToken(user.getEmail(), user.getId(), user.getUsername());
            return ResponseEntity.ok(new AOuthResponse(fbUser.getEmail() , jwt));
        }
    }

    @PostMapping("/google/login")
    public ResponseEntity<?> loginWithGoogle(@RequestBody JWTResponseDto request) {
        User googleUser = userService.getGoogleUser(request.getAccessToken());

        if (googleUser == null || googleUser.getEmail() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Google ID token");
        }

        boolean exists=userService.isExist(googleUser.getEmail());
        if (!exists) {
            userService.save(googleUser); // Register new user
            return ResponseEntity.ok(new AOuthResponse(googleUser.getEmail()));
        }
        else {
            User user = userService.loadUserByEmail(googleUser.getEmail());

            if (!user.getProvider().equals("GOOGLE")) {
                throw new BaseApiExceptions(
                        String.format("This Email is registered via Facebook", user.getEmail()),
                        HttpStatus.NOT_FOUND
                );
            }

            UserInfo userInfo = userInfoService.loadUserInfoById(user.getId());
            if(userInfo == null) {
                return ResponseEntity.ok(new AOuthResponse(user.getEmail()));
            }

            String jwt = jwtTokenUtils.generateToken(user.getEmail(), user.getId(), user.getUsername());
            return ResponseEntity.ok(new AOuthResponse(googleUser.getEmail(), jwt));

        }
    }

    @PostMapping("/register")
    public Response register (@Valid @RequestBody User user){
        return userService.save(user);
    }

    @PostMapping("/saveUserInfo")
    public Response saveUserInfoApi (@Valid @RequestBody UserInfo userInfo, @RequestParam String userEmail){
        return userRegistrationService.registerUserAndAssignPlan(userInfo, userEmail);
    }

    @GetMapping("favicon.ico")
    @ResponseBody
    public void returnNoFavicon() {
        // Returning nothing to prevent error
    }
}
