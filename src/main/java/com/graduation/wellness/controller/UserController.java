package com.graduation.wellness.controller;
import com.graduation.wellness.security.JwtTokenUtils;
import com.graduation.wellness.service.EmailService;
import com.graduation.wellness.service.UserInfoService;
import com.graduation.wellness.service.UserService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.graduation.wellness.model.dto.UserInfoDTO;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@AllArgsConstructor
public class UserController {

    private UserService userService;
    private final UserInfoService userInfoService;
    private EmailService emailService;
    private final JwtTokenUtils jwtTokenUtils;

    @GetMapping("/getUserInfo")
    public UserInfoDTO getUserInfoApi(){
        return userInfoService.getUserInfo();
    }

    @PostMapping("/active")
    public ResponseEntity<String> sendOTPMail(@RequestParam String username , @RequestParam String email) throws MessagingException {
        String code=emailService.verificationMail(username, email);
        return ResponseEntity.ok(code);
    }

    @PostMapping("/changePasswordRequest")
    public ResponseEntity<String> changePasswordMail(@RequestParam String email) throws MessagingException {
        String code= emailService.changePassswordMail(email);

        return ResponseEntity.ok(code);
    }

    @PostMapping("/changePassword")
    public Map changePassword(@RequestParam String email,@RequestParam String password) {
        userService.changePassword(email , password);

        Map<String ,String> map = new HashMap<>();
        map.put("status" , "success");
        map.put("message" ,"Password has been changed successfully!");
        return map;
    }

    @DeleteMapping("/deleteAccount")
    public ResponseEntity<String> deleteAccount() {
        String jwtToken = jwtTokenUtils.getJwtToken();
        String email = jwtTokenUtils.getEmailFromToken(jwtToken);
        userService.deleteAccount(email);
        return ResponseEntity.ok("Account has been deleted");
    }
}