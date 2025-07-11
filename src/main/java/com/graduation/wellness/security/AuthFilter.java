package com.graduation.wellness.security;

import com.graduation.wellness.model.entity.User;
import com.graduation.wellness.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Log4j2
@Component
public class AuthFilter extends GenericFilterBean {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtils tokenUtil;

    @Autowired
    public AuthFilter(JwtTokenUtils tokenUtil, UserService userService) {
        this.tokenUtil = tokenUtil;
        this.userService = userService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String jwtTokenHeader = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);

        final SecurityContext securityContext = SecurityContextHolder.getContext();

        if (jwtTokenHeader != null && securityContext.getAuthentication() == null) {
            String jwtToken = jwtTokenHeader.substring("Bearer ".length());

            try {
                tokenUtil.setJwtToken(jwtToken);
                String email = tokenUtil.getEmailFromToken(jwtToken); // ⚠ This can throw ExpiredJwtException

                if (email != null && tokenUtil.validateToken(jwtToken)) {
                    User user = userService.loadUserByEmail(email);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            user, null, user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

            } catch (io.jsonwebtoken.ExpiredJwtException e) {
                log.warn("JWT token has expired: {}", e.getMessage());

                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpResponse.setContentType("application/json");
                httpResponse.getWriter().write("{\"error\": \"TokenExpired\", \"message\": \"JWT token has expired\"}");
                return;

            } catch (Exception e) {
                log.error("JWT processing error: {}", e.getMessage());

                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpResponse.setContentType("application/json");
                httpResponse.getWriter().write("{\"error\": \"InvalidToken\", \"message\": \"Token is invalid\"}");
                return;
            }
        }

        chain.doFilter(request, response);
    }

//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//
//        String jwtTokenHeader = ((HttpServletRequest) request).getHeader(HttpHeaders.AUTHORIZATION);
//
//        final SecurityContext securityContext = SecurityContextHolder.getContext();
//
//        if (jwtTokenHeader != null && securityContext.getAuthentication() == null) {
//            String jwtToken = jwtTokenHeader.substring("Bearer ".length());
//            tokenUtil.setJwtToken(jwtToken);
//            String Email = tokenUtil.getEmailFromToken(jwtToken);
//            if (Email != null && tokenUtil.validateToken(jwtToken)) {;
//                User user =  userService.loadUserByEmail(Email);
//                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//                        user, null, user.getAuthorities());
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            }
//        }
//        chain.doFilter(request, response);
//    }
}