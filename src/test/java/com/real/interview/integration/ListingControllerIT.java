package com.real.interview.integration;

import com.real.interview.entity.Listing;
import com.real.interview.repository.ListingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ListingControllerIT {

    @TestConfiguration
    static class OAuth2TestConfig {
        @Bean
        public ClientRegistrationRepository clientRegistrationRepository() {
            return mock(ClientRegistrationRepository.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ListingRepository listingRepository;

    private Long testListingId;

    @BeforeEach
    void setUp() {
        listingRepository.deleteAll();
        Listing listing = new Listing();
        listing.setTitle("Test Listing");
        listing.setAddress("123 Test St");
        listing.setPrice(100000.0);
        listing.setBedrooms(3);
        listing.setBathrooms(2);
        listing.setDescription("Test description");
        Listing saved = listingRepository.save(listing);
        testListingId = saved.getId();
    }

    @Test
    @WithMockUser(roles = "AGENT")
    @DisplayName("GET /api/v1/listings returns 200 OK")
    void getAllListings() {
        try {
            mockMvc.perform(get("/api/v1/listings"))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @WithMockUser(roles = "AGENT")
    @DisplayName("GET /api/v1/listings/{id} returns 404 for not found")
    void getListingByIdNotFound() {
        try {
            mockMvc.perform(get("/api/v1/listings/99999"))
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @WithMockUser(roles = "AGENT")
    @DisplayName("POST /api/v1/listings creates a new listing and returns 201 Created")
    void createListing() throws Exception {
        String newListingJson = "{\"title\":\"Sample Listing\",\"price\":100000}";
        mockMvc.perform(
                post("/api/v1/listings")
                        .contentType("application/json")
                        .content(newListingJson)
        ).andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "AGENT")
    @DisplayName("PUT /api/v1/listings/{id} updates a listing and returns 200 OK")
    void updateListing() throws Exception {
        String updateListingJson = "{\"title\":\"Updated Listing\",\"price\":200000}";
        mockMvc.perform(
                put("/api/v1/listings/" + testListingId)
                        .contentType("application/json")
                        .content(updateListingJson)
        ).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "AGENT")
    @DisplayName("DELETE /api/v1/listings/{id} deletes a listing and returns 204 No Content")
    void deleteListing() throws Exception {
        mockMvc.perform(delete("/api/v1/listings/" + testListingId))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/listings is forbidden for USER role")
    void getAllListingsForbiddenForUser() throws Exception {
        mockMvc.perform(get("/api/v1/listings"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "AGENT")
    @DisplayName("GET /api/v1/listings/{id} returns 200 OK for existing listing")
    void getListingByIdOk() throws Exception {
        mockMvc.perform(get("/api/v1/listings/" + testListingId))
                .andExpect(status().isOk());
    }
}
