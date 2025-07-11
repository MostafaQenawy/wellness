package com.graduation.wellness.controller;
import com.graduation.wellness.model.dto.Response;
import com.graduation.wellness.model.dto.UserInfoDTO;
import com.graduation.wellness.service.EmailService;
import com.graduation.wellness.service.UserInfoService;
import com.graduation.wellness.service.UserService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@AllArgsConstructor
public class UserController {

    private UserService userService;
    private final UserInfoService userInfoService;
    private EmailService emailService;

    @GetMapping("/getUserInfo")
    public UserInfoDTO getUserInfoApi(){
        return userInfoService.getUserInfo();
    }

    @PostMapping("/active")
    public Map<String ,String> sendOTPMail(@RequestParam String userName, @RequestParam String email)
            throws MessagingException {
        return emailService.verificationMail(userName, email);
    }

    @PostMapping("/changePasswordRequest")
    public Map<String ,String> changePasswordMail(@RequestParam String email) throws MessagingException {
        return emailService.changePassswordMail(email);
    }

    @PostMapping("/changePassword")
    public Response changePassword(@RequestParam String email, @RequestParam String password) {
        return userService.changePassword(email , password);
    }

    @PostMapping("/changePasswordInternal")
    public Response changePasswordInternal
            (@RequestParam String curPassword,@RequestParam String newPassword) {
        return userService.changePasswordInternal(curPassword, newPassword);
    }

    @DeleteMapping("/deleteAccount")
    public Response deleteAccount() {
        return userService.deleteAccount();
    }
    @PostMapping("/updateProfilePicture")
    public Response updateProfilePicture(@RequestParam("file") MultipartFile file) throws IOException {
        return userService.updateProfilePicture(file);
    }

    @GetMapping("/getProfilePicture")
    public ResponseEntity<byte[]> getProfilePicture() {
        return userService.getProfilePicture();
    }

    @PostMapping("/updateProfile")
    public Response updateAccount(@RequestBody UserInfoDTO userInfoDTO) {
        return userService.updateAccount(userInfoDTO);
    }

}