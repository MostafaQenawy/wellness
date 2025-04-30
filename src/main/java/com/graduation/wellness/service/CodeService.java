package com.graduation.wellness.service;

import com.graduation.wellness.model.entity.EmailTemplateName;
import com.graduation.wellness.model.entity.ActivationCode;
import com.graduation.wellness.model.entity.User;
import com.graduation.wellness.repository.ActivationCodeRepo;
import com.graduation.wellness.repository.UserRepo;
import jakarta.mail.MessagingException;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CodeService {

    private final ActivationCodeRepo activationCodeRepo;
    private final EmailService emailService;
    private final UserService userService;
    private final UserRepo userRepo;

    public String sendOTPMail(String email , EmailTemplateName emailTemplateName) throws MessagingException {
        User user  = userService.loadUserByEmail(email);
        String code = generateAndSendCode(user);
        sendValidationEmail(user , code , emailTemplateName);
        return code;
    }

    public String generateAndSendCode(User user) {

        activationCodeRepo.findByUser(user).ifPresent(activationCodeRepo::delete);
        String code =generateActivationCode(6); // 6-digit code
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(2); // Expire in 1 min
        ActivationCode activationCode = new ActivationCode(user, code, expiryTime);
        activationCodeRepo.save(activationCode);
        return code;
    }

    private String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();

        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }

        return codeBuilder.toString();
    }

    public void sendValidationEmail(User user , String activationCode , EmailTemplateName emailTemplateName) throws MessagingException {
        emailService.sendEmail(
                user.getEmail(),
                user.getUsername(),
                emailTemplateName,
                activationCode
        );
    }


    public boolean validateCode(String email, String code) {
        User user = userService.loadUserByEmail(email);

        Optional<ActivationCode> activationCodeOpt = activationCodeRepo.findByUser(user);

        if (activationCodeOpt.isEmpty())
            return false; // No activation code found

        ActivationCode activationCode = activationCodeOpt.get();
        if (activationCode.getExpiryTime().isBefore(LocalDateTime.now())) {
            activationCodeRepo.delete(activationCode); // Expired, delete it
            return false;
        }

        if (!activationCode.getCode().equals(code))
            return false; // Incorrect code

        //delete activation code
        activationCodeRepo.delete(activationCode);

        return true;
    }

    public boolean validateUser(String email, String code) {
        boolean validCode = validateCode(email, code);
        if (validCode){
            User user = userService.loadUserByEmail(email);
            userService.activateUser(user);
        }
        return validCode;
    }

    @Transactional
    @Scheduled(fixedRate = 60000) // Run every 1 minute
    public void removeExpiredActivationCodes() {
        activationCodeRepo.deleteByExpiryTimeBefore(LocalDateTime.now());
    }

}