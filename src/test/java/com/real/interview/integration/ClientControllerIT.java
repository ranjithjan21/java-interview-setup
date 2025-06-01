package com.real.interview.integration;

import com.real.interview.entity.Client;
import com.real.interview.repository.ClientRepository;
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
class ClientControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientRepository clientRepository;

    private Long testClientId;

    @BeforeEach
    void setUp() {
        clientRepository.deleteAll();
        Client client = new Client();
        client.setName("Alice Smith");
        client.setEmail("alice@example.com");
        client.setPhone("5551234567");
        Client saved = clientRepository.save(client);
        testClientId = saved.getId();
    }

    @Test
    @DisplayName("GET /api/v1/clients returns 200 OK")
    void getAllClients() throws Exception {
        mockMvc.perform(get("/api/v1/clients"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("GET /api/v1/clients/{id} returns 200 OK for existing client")
    void getClientByIdOk() throws Exception {
        mockMvc.perform(get("/api/v1/clients/" + testClientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice Smith"));
    }

    @Test
    @DisplayName("GET /api/v1/clients/{id} returns 404 for not found")
    void getClientByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/clients/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/v1/clients creates a new client and returns 200 OK")
    void createClient() throws Exception {
        String newClientJson = "{\"name\":\"Bob Brown\",\"email\":\"bob@example.com\",\"phone\":\"5559876543\"}";
        mockMvc.perform(post("/api/v1/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newClientJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Bob Brown"));
    }

    @Test
    @DisplayName("PUT /api/v1/clients/{id} updates a client and returns 200 OK")
    void updateClient() throws Exception {
        String updateClientJson = "{\"name\":\"Updated Name\",\"email\":\"updated@example.com\",\"phone\":\"1112223333\"}";
        mockMvc.perform(put("/api/v1/clients/" + testClientId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateClientJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"));
    }

    @Test
    @DisplayName("DELETE /api/v1/clients/{id} deletes a client and returns 204 No Content")
    void deleteClient() throws Exception {
        mockMvc.perform(delete("/api/v1/clients/" + testClientId))
                .andExpect(status().isNoContent());
    }
}

