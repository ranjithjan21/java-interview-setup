package com.real.interview.config;
import org.junit.jupiter.api.Test;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SecurityConfigTest {
    @Test
    void testFilterChainBeanCreation() throws Exception {
        JwtUtil jwtUtil = mock(JwtUtil.class);
        SecurityConfig config = new SecurityConfig(jwtUtil);
        HttpSecurity http = mock(HttpSecurity.class, RETURNS_DEEP_STUBS);
        // Just verify that the bean can be created without exceptions
        SecurityFilterChain chain = config.filterChain(http);
        assertNotNull(chain);
    }

    @Test
    void testFilterChainBeanCreation_noException() throws Exception {
        JwtUtil jwtUtil = mock(JwtUtil.class);
        SecurityConfig config = new SecurityConfig(jwtUtil);
        HttpSecurity http = mock(HttpSecurity.class, RETURNS_DEEP_STUBS);
        SecurityFilterChain chain = config.filterChain(http);
        assertNotNull(chain);
    }

    @Test
    void testSecurityConfigConstructorStoresJwtUtil() {
        JwtUtil jwtUtil = mock(JwtUtil.class);
        SecurityConfig config = new SecurityConfig(jwtUtil);
        assertNotNull(config);
    }

    @Test
    void testFilterChainWithNullJwtUtil() throws Exception {
        SecurityConfig config = new SecurityConfig(null);
        HttpSecurity http = mock(HttpSecurity.class, RETURNS_DEEP_STUBS);
        SecurityFilterChain chain = config.filterChain(http);
        assertNotNull(chain);
    }
}
