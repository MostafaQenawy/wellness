package com.graduation.wellness.service;


import com.graduation.wellness.model.enums.EmailTemplateName;
import com.graduation.wellness.model.entity.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_MIXED;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final UserService userService;

    @Async
    public void sendEmail(
            String to,
            String username,
            EmailTemplateName emailTemplate,
            String code
    ) throws MessagingException {
        String templateName;
        if (emailTemplate == null) {
            templateName = "confirm-email";
        } else {
            templateName = emailTemplate.name();
        }
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                mimeMessage,
                MULTIPART_MODE_MIXED,
                UTF_8.name()
        );
        Map<String, Object> properties = new HashMap<>();
        properties.put("username", username);
        properties.put("code", code);

        Context context = new Context();
        context.setVariables(properties);

        helper.setFrom("wellness.hub.app@gmail.com");
        helper.setTo(to);
        helper.setSubject(emailTemplate.getName());

        String template = templateEngine.process(templateName, context);

        helper.setText(template, true);

        mailSender.send(mimeMessage);
    }


    public String changePassswordMail(String email ) throws MessagingException {
        User user  = userService.loadUserByEmail(email);
        String code =generateActivationCode(6);
        sendEmail(
                user.getEmail(),
                user.getUsername(),
                EmailTemplateName.CHANGE_PASSWORD,
                code
        );
        return code;
    }

    public String verificationMail(String username , String email ) throws MessagingException {
        String code =generateActivationCode(6);
        sendEmail(
                email,
                username,
                EmailTemplateName.ACTIVATE_ACCOUNT,
                code
        );
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
}