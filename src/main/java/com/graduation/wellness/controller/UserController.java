package com.graduation.wellness.controller;
import com.graduation.wellness.security.JwtTokenUtils;
import com.graduation.wellness.service.EmailService;
import com.graduation.wellness.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.HashMap;
import java.util.Map;


@RestController
@Slf4j
@AllArgsConstructor
public class UserController {


    private UserService userService;
    private EmailService emailService;
    private JwtTokenUtils jwtTokenUtils;


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
