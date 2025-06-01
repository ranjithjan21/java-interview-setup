package com.real.interview.controller;

import com.real.interview.entity.Client;
import com.real.interview.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ClientControllerTest {

    private MockMvc mockMvc;
    @Mock
    private ClientRepository clientRepository;
    @InjectMocks
    private ClientController clientController;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(clientController).build();
    }

    @Test
    @DisplayName("GET /api/v1/clients returns list")
    void getAllClients() throws Exception {
        Mockito.when(clientRepository.findAll()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/v1/clients"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/v1/clients/{id} returns 200 if found")
    void getClientByIdFound() throws Exception {
        Client client = new Client();
        Mockito.when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        mockMvc.perform(get("/api/v1/clients/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/v1/clients/{id} returns 404 if not found")
    void getClientByIdNotFound() throws Exception {
        Mockito.when(clientRepository.findById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/v1/clients/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/v1/clients creates client")
    void createClient() throws Exception {
        Client client = new Client();
        Mockito.when(clientRepository.save(any(Client.class))).thenReturn(client);
        mockMvc.perform(post("/api/v1/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PUT /api/v1/clients/{id} updates client if found")
    void updateClientFound() throws Exception {
        Client client = new Client();
        Mockito.when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        Mockito.when(clientRepository.save(any(Client.class))).thenReturn(client);
        mockMvc.perform(put("/api/v1/clients/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PUT /api/v1/clients/{id} returns 404 if not found")
    void updateClientNotFound() throws Exception {
        Mockito.when(clientRepository.findById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(put("/api/v1/clients/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/v1/clients/{id} returns 204 if deleted")
    void deleteClientFound() throws Exception {
        Mockito.when(clientRepository.existsById(1L)).thenReturn(true);
        mockMvc.perform(delete("/api/v1/clients/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/v1/clients/{id} returns 404 if not found")
    void deleteClientNotFound() throws Exception {
        Mockito.when(clientRepository.existsById(1L)).thenReturn(false);
        mockMvc.perform(delete("/api/v1/clients/1"))
                .andExpect(status().isNotFound());
    }
}
