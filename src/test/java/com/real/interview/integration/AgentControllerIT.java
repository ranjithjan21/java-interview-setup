package com.real.interview.integration;

import com.real.interview.entity.Agent;
import com.real.interview.repository.AgentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AgentControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AgentRepository agentRepository;

    private Long testAgentId;

    @BeforeEach
    void setUp() {
        agentRepository.deleteAll();
        Agent agent = new Agent();
        agent.setName("John Doe");
        agent.setEmail("john@example.com");
        agent.setPhone("1234567890");
        Agent saved = agentRepository.save(agent);
        testAgentId = saved.getId();
    }

    @Test
    @DisplayName("GET /api/v1/agents returns 200 OK")
    void getAllAgents() throws Exception {
        mockMvc.perform(get("/api/v1/agents"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("GET /api/v1/agents/{id} returns 200 OK for existing agent")
    void getAgentByIdOk() throws Exception {
        mockMvc.perform(get("/api/v1/agents/" + testAgentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    @DisplayName("GET /api/v1/agents/{id} returns 404 for not found")
    void getAgentByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/agents/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/v1/agents creates a new agent and returns 200 OK")
    void createAgent() throws Exception {
        String newAgentJson = "{\"name\":\"Jane Smith\",\"email\":\"jane@example.com\",\"phone\":\"9876543210\"}";
        mockMvc.perform(post("/api/v1/agents")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newAgentJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Jane Smith"));
    }

    @Test
    @DisplayName("PUT /api/v1/agents/{id} updates an agent and returns 200 OK")
    void updateAgent() throws Exception {
        String updateAgentJson = "{\"name\":\"Updated Name\",\"email\":\"updated@example.com\",\"phone\":\"1112223333\"}";
        mockMvc.perform(put("/api/v1/agents/" + testAgentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateAgentJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"));
    }

    @Test
    @DisplayName("DELETE /api/v1/agents/{id} deletes an agent and returns 204 No Content")
    void deleteAgent() throws Exception {
        mockMvc.perform(delete("/api/v1/agents/" + testAgentId))
                .andExpect(status().isNoContent());
    }
}


