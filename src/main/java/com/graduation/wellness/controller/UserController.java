package com.graduation.wellness.controller;
import com.graduation.wellness.security.JWTResponseDto;
import com.graduation.wellness.security.JwtRequestDto;
import com.graduation.wellness.service.ActivationCodeService;
import com.graduation.wellness.service.UserService;
import com.graduation.wellness.model.dto.UserDto;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;


@RestController
@Slf4j
@AllArgsConstructor
public class UserController {


    private UserService userService;
    private ActivationCodeService activationCodeService;

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

    @PostMapping("/active")
    public ResponseEntity<String> activeAccount(@RequestParam String email) throws MessagingException {
        activationCodeService.activateAccount(email);

        return ResponseEntity.ok("Activation email has been sent");
    }


    @PostMapping("/verify")
    public ResponseEntity<String> verifyCode(@RequestParam String email, @RequestParam String code) {
        boolean isVerified = activationCodeService.validateCode(email, code);
        if (isVerified){
            return ResponseEntity.ok("Account activated successfully!");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid activation code.");
    }
}
