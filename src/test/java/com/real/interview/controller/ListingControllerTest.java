package com.real.interview.controller;

import com.real.interview.dto.ListingDto;
import com.real.interview.service.ListingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ListingControllerTest {
    private MockMvc mockMvc;
    @Mock
    private ListingService listingService;
    @InjectMocks
    private ListingController listingController;

    @org.junit.jupiter.api.BeforeEach
    void setup() {
        mockMvc = org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup(listingController).build();
    }

    @Test
    @DisplayName("GET /api/v1/listings returns list")
    void getAllListings() throws Exception {
        Mockito.when(listingService.getAll()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/v1/listings"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/v1/listings/{id} returns 200 if found")
    void getListingByIdFound() throws Exception {
        ListingDto dto = new ListingDto();
        Mockito.when(listingService.getById(1L)).thenReturn(Optional.of(dto));
        mockMvc.perform(get("/api/v1/listings/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/v1/listings/{id} returns 404 if not found")
    void getListingByIdNotFound() throws Exception {
        Mockito.when(listingService.getById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/v1/listings/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/v1/listings creates listing")
    void createListing() throws Exception {
        ListingDto dto = new ListingDto();
        Mockito.when(listingService.create(any(ListingDto.class))).thenReturn(dto);
        mockMvc.perform(post("/api/v1/listings")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("PUT /api/v1/listings/{id} updates listing if found")
    void updateListingFound() throws Exception {
        ListingDto dto = new ListingDto();
        Mockito.when(listingService.update(eq(1L), any(ListingDto.class))).thenReturn(Optional.of(dto));
        mockMvc.perform(put("/api/v1/listings/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PUT /api/v1/listings/{id} returns 404 if not found")
    void updateListingNotFound() throws Exception {
        Mockito.when(listingService.update(eq(1L), any(ListingDto.class))).thenReturn(Optional.empty());
        mockMvc.perform(put("/api/v1/listings/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/v1/listings/{id} returns 204 if deleted")
    void deleteListingFound() throws Exception {
        Mockito.when(listingService.delete(1L)).thenReturn(true);
        mockMvc.perform(delete("/api/v1/listings/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/v1/listings/{id} returns 404 if not found")
    void deleteListingNotFound() throws Exception {
        Mockito.when(listingService.delete(1L)).thenReturn(false);
        mockMvc.perform(delete("/api/v1/listings/1"))
                .andExpect(status().isNotFound());
    }
}
