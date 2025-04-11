package com.graduation.wellness.service;

import com.graduation.wellness.email.EmailService;
import com.graduation.wellness.email.EmailTemplateName;
import com.graduation.wellness.model.entity.ActivationCode;
import com.graduation.wellness.model.entity.User;
import com.graduation.wellness.repository.ActivationCodeRepo;
import com.graduation.wellness.repository.UserRepo;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ActivationCodeService {

    private final ActivationCodeRepo activationCodeRepo;
    private final EmailService emailService;
    private final UserService userService;
    private final UserRepo userRepo;

    public void activateAccount(String email) throws MessagingException {
        User user  = userService.loadUserByEmail(email);
        String code = generateAndSendActivationCode(user);
        sendValidationEmail(user , code);
    }

    public String generateAndSendActivationCode(User user) {

        activationCodeRepo.findByUser(user).ifPresent(activationCodeRepo::delete);
        String code =generateActivationCode(6); // 6-digit code
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(1); // Expire in 1 min
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

    public void sendValidationEmail(User user , String activationCode) throws MessagingException {
        emailService.sendEmail(
                user.getEmail(),
                user.getUsername(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationCode,
                "Account activation"
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

        // Activate user and delete activation code
        user.setVerified(true);
        userRepo.save(user);
        activationCodeRepo.delete(activationCode);

        return true;
    }

    @Scheduled(fixedRate = 60000) // Run every 1 minute
    public void removeExpiredActivationCodes() {
        activationCodeRepo.deleteByExpiryTimeBefore(LocalDateTime.now());
    }

}