package com.real.interview.controller;

import com.real.interview.entity.Agent;
import com.real.interview.repository.AgentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AgentControllerTest {

    private MockMvc mockMvc;
    @Mock
    private AgentRepository agentRepository;
    @InjectMocks
    AgentController agentController;

    @org.junit.jupiter.api.BeforeEach
    void setup() {
        mockMvc = org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup(agentController).build();
    }

    @Test
    @DisplayName("GET /api/v1/agents returns list")
    void getAllAgents() throws Exception {
        when(agentRepository.findAll()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/v1/agents"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/v1/agents/{id} returns 200 if found")
    void getAgentByIdFound() throws Exception {
        Agent agent = new Agent();
        when(agentRepository.findById(1L)).thenReturn(Optional.of(agent));
        mockMvc.perform(get("/api/v1/agents/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/v1/agents/{id} returns 404 if not found")
    void getAgentByIdNotFound() throws Exception {
        when(agentRepository.findById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/v1/agents/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/v1/agents creates agent")
    void createAgent() throws Exception {
        Agent agent = new Agent();
        when(agentRepository.save(any(Agent.class))).thenReturn(agent);
        mockMvc.perform(post("/api/v1/agents")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PUT /api/v1/agents/{id} updates agent if found")
    void updateAgentFound() throws Exception {
        Agent agent = new Agent();
        when(agentRepository.findById(1L)).thenReturn(Optional.of(agent));
        when(agentRepository.save(any(Agent.class))).thenReturn(agent);
        mockMvc.perform(put("/api/v1/agents/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PUT /api/v1/agents/{id} returns 404 if not found")
    void updateAgentNotFound() throws Exception {
        when(agentRepository.findById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(put("/api/v1/agents/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/v1/agents/{id} returns 204 if deleted")
    void deleteAgentFound() throws Exception {
        when(agentRepository.existsById(1L)).thenReturn(true);
        mockMvc.perform(delete("/api/v1/agents/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/v1/agents/{id} returns 404 if not found")
    void deleteAgentNotFound() throws Exception {
        when(agentRepository.existsById(1L)).thenReturn(false);
        mockMvc.perform(delete("/api/v1/agents/1"))
                .andExpect(status().isNotFound());
    }
}
