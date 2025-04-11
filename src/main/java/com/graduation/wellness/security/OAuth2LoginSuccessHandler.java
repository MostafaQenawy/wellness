package com.graduation.wellness.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.graduation.wellness.model.entity.User;
import com.graduation.wellness.service.RoleService;
import com.graduation.wellness.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenUtils jwtTokenUtils;
    private final UserService userService;
    private final RoleService roleService;

    public OAuth2LoginSuccessHandler(JwtTokenUtils jwtTokenUtils, UserService userService, RoleService roleService) {
        this.jwtTokenUtils = jwtTokenUtils;
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String provider = "GOOGLE"; // Default provider
        String providerUserId = oAuth2User.getAttribute("sub"); // Google ID

        String firstName = oAuth2User.getAttribute("given_name"); // Google first name
        String lastName = oAuth2User.getAttribute("family_name"); // Google last name

        if (oAuth2User.getAttribute("id") != null) { // If it's Facebook
            provider = "FACEBOOK";
            providerUserId = oAuth2User.getAttribute("id"); // Facebook ID

            // 🔥 Facebook provides "name" instead of "first_name" and "last_name"
            String fullName = oAuth2User.getAttribute("name"); // Full name (e.g., "John Doe")

            if (fullName != null) {
                String[] nameParts = fullName.split(" ", 2);
                firstName = nameParts[0]; // First word as first name
                lastName = (nameParts.length > 1) ? nameParts[1] : ""; // Rest as last name
            }
        }
        if (firstName == null || firstName.isEmpty()) {
            firstName = "Unknown"; // Default value
        }

        if (lastName == null) {
            lastName = ""; // Or set it to "Unknown"
        }

        // Check if user exists in the database
        boolean isNewUser = userService.isExist(email);
        User user;

        if ( ! isNewUser) {
            // Register new user without password
            user = new User();
            user.setEmail(email);
            user.setSyncAccount(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setProvider(provider);
            user.setProviderUserId(providerUserId);
            user.setVerified(true);
            userService.save(user);
        }else {
            user = userService.loadUserByEmail(email);
        }

        // Generate JWT token
        String token = jwtTokenUtils.generateToken(user.getEmail(), user.getId());

        // ✅ Store token in session (or URL param)
        request.getSession().setAttribute("jwtToken", token);

        // ✅ Redirect to /facebook
        response.sendRedirect("/provider");

    }

}
