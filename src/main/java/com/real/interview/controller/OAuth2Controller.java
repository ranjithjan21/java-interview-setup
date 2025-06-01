package com.real.interview.controller;

import com.real.interview.config.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class OAuth2Controller {
    private final JwtUtil jwtUtil;

    public OAuth2Controller(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/oauth2/success")
    public void getJwtToken(Authentication authentication, HttpServletResponse response) throws IOException {
        if (authentication == null || !(authentication.getPrincipal() instanceof OAuth2User)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return;
        }
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        Map<String, Object> claims = new HashMap<>();
        claims.put("name", oAuth2User.getAttribute("name"));
        claims.put("email", email);
        // Assign role based on email
        String role;
        switch (email) {
            case "admin.realapp@gmail.com":
                role = "ADMIN";
                break;
            case "client.realapp@gmail.com":
                role = "CLIENT";
                break;
            case "agent.realapp@gmail.com":
                role = "AGENT";
                break;
            default:
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
                return;
        }
        claims.put("role", role);
        String token = jwtUtil.generateToken(email, claims);
        response.setContentType("application/json");
        response.getWriter().write("{\"token\": \"" + token + "\"}");
    }
}

