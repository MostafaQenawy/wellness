package com.graduation.wellness.controller;
import com.graduation.wellness.model.entity.EmailTemplateName;
import com.graduation.wellness.service.CodeService;
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
    private CodeService CodeService;

    @GetMapping("/user/{id}")
    public ResponseEntity<?> findById(@Valid @PathVariable Long id){
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping("/profile-picture/{userId}")
    public ResponseEntity<Resource> getProfilePicture(@PathVariable String userId) {
        try {
            Path imagePath = Paths.get("uploads/profile_pictures/" + userId + ".jpg");
            Resource resource = new UrlResource(imagePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/api/v1")
    public String Hello(){
        return "Hello from springboot and Keycloak";
    }

    @GetMapping("/protected-endpoint")
    public ResponseEntity<String> getProtectedData(Authentication authentication) {
        log.info("Current Authentication: " + authentication);
        if (authentication != null) {
            return ResponseEntity.ok("Access granted to " + authentication.getName());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
    }
//OTP
    @PostMapping("/active")
    public ResponseEntity<String> activeAccount(@RequestParam String email) throws MessagingException {
        String code=CodeService.sendOTPMail(email , EmailTemplateName.ACTIVATE_ACCOUNT);

        return ResponseEntity.ok(code);
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestParam String email, @RequestParam String code) {
        boolean isVerified = CodeService.validateUser(email, code );
        if (isVerified){
            return ResponseEntity.ok("Account activated successfully!");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid activation code.");
    }

    @PostMapping("/changePasswordRequest")
    public ResponseEntity<String> changePasswordMail(@RequestParam String email) throws MessagingException {
        String code=CodeService.sendOTPMail(email , EmailTemplateName.CHANGE_PASSWORD);

        return ResponseEntity.ok(code);
    }

    @PostMapping("/validate")
    public ResponseEntity<String> validateCode(@RequestParam String email, @RequestParam String code) {
        boolean isValid = CodeService.validateCode(email, code );
        if (isValid){
            return ResponseEntity.ok("Valid Code");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Code.");
    }

    @PostMapping("/changePassword")
    public Map changePassword(@RequestParam String email, @RequestParam String password) {
        userService.changePassword(email , password);

        Map<String ,String> map = new HashMap<>();
        map.put("status" , "success");
        map.put("message" ,"Password has been changed successfully!");
        return map;
    }

    @DeleteMapping("/deleteAccount")
    public ResponseEntity<String> deleteAccount(@RequestParam String email) {
        userService.deleteAccount(email);
        return ResponseEntity.ok("Account has been deleted");
    }

}
