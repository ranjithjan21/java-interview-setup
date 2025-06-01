package com.real.interview.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        // 32 chars for HS256
        String secret = "0123456789abcdef0123456789abcdef";
        ReflectionTestUtils.setField(jwtUtil, "jwtSecret", secret);
        // 1 hour
        long expiration = 1000 * 60 * 60;
        ReflectionTestUtils.setField(jwtUtil, "jwtExpirationMs", expiration);
    }

    @Test
    void testGenerateAndValidateToken() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "ADMIN");
        String token = jwtUtil.generateToken("user@example.com", claims);
        assertNotNull(token);
        assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    void testExtractUsername() {
        Map<String, Object> claims = new HashMap<>();
        String token = jwtUtil.generateToken("user@example.com", claims);
        String username = jwtUtil.extractUsername(token);
        assertEquals("user@example.com", username);
    }

    @Test
    void testExtractClaim() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "ADMIN");
        String token = jwtUtil.generateToken("user@example.com", claims);
        String role = jwtUtil.extractClaim(token, "role", String.class);
        assertEquals("ADMIN", role);
    }

    @Test
    void testValidateToken_invalid() {
        assertFalse(jwtUtil.validateToken("invalid.token"));
    }
}

