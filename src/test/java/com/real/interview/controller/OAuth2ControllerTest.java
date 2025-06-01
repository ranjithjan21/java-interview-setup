package com.real.interview.controller;

import com.real.interview.config.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class OAuth2ControllerTest {
    private MockMvc mockMvc;
    @Mock
    private JwtUtil jwtUtil;
    @InjectMocks
    private OAuth2Controller oAuth2Controller;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(oAuth2Controller).build();
    }

    @Test
    @DisplayName("GET /oauth2/success returns token for ADMIN email")
    void getJwtToken_Admin() throws Exception {
        Authentication authentication = Mockito.mock(Authentication.class);
        OAuth2User oAuth2User = Mockito.mock(OAuth2User.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(oAuth2User);
        Mockito.when(oAuth2User.getAttribute("email")).thenReturn("admin.realapp@gmail.com");
        Mockito.when(oAuth2User.getAttribute("name")).thenReturn("Example User");
        Mockito.when(jwtUtil.generateToken(eq("admin.realapp@gmail.com"), any(Map.class))).thenReturn("test-token");
        mockMvc.perform(get("/oauth2/success").principal(authentication))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"token\": \"test-token\"}"));
    }

    @Test
    @DisplayName("GET /oauth2/success returns 401 if not authenticated")
    void getJwtToken_Unauthorized() throws Exception {
        mockMvc.perform(get("/oauth2/success"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("GET /oauth2/success returns 403 for unknown email")
    void getJwtToken_Forbidden() throws Exception {
        Authentication authentication = Mockito.mock(Authentication.class);
        OAuth2User oAuth2User = Mockito.mock(OAuth2User.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(oAuth2User);
        Mockito.when(oAuth2User.getAttribute("email")).thenReturn("unknown@example.com");
        Mockito.when(oAuth2User.getAttribute("name")).thenReturn("Unknown");
        mockMvc.perform(get("/oauth2/success").principal(authentication))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("GET /oauth2/success returns 401 if principal is not OAuth2User")
    void getJwtToken_NotOAuth2User() throws Exception {
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn("not-an-oauth2user");
        mockMvc.perform(get("/oauth2/success").principal(authentication))
                .andExpect(status().isUnauthorized());
    }
}
