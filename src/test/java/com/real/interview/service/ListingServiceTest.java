package com.real.interview.service;

import com.real.interview.dto.ListingDto;
import com.real.interview.entity.Listing;
import com.real.interview.repository.AgentRepository;
import com.real.interview.repository.ClientRepository;
import com.real.interview.repository.ListingRepository;
import com.real.interview.transformer.ListingTransformer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ListingServiceTest {
    @Mock
    private ListingRepository listingRepo;
    @Mock
    private AgentRepository agentRepo;
    @Mock
    private ClientRepository clientRepo;
    @Mock
    private ListingTransformer transformer;
    @InjectMocks
    private ListingService listingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        listingService = new ListingService(listingRepo, agentRepo, clientRepo, transformer);
    }

    @Test
    @DisplayName("getAll returns list of ListingDto")
    void testGetAll() {
        Listing listing = new Listing();
        ListingDto dto = new ListingDto();
        Mockito.when(listingRepo.findAll()).thenReturn(List.of(listing));
        Mockito.when(transformer.toDto(listing)).thenReturn(dto);
        List<ListingDto> result = listingService.getAll();
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("getById returns ListingDto if found")
    void testGetByIdFound() {
        Listing listing = new Listing();
        ListingDto dto = new ListingDto();
        Mockito.when(listingRepo.findById(1L)).thenReturn(Optional.of(listing));
        Mockito.when(transformer.toDto(listing)).thenReturn(dto);
        Optional<ListingDto> result = listingService.getById(1L);
        assertTrue(result.isPresent());
    }

    @Test
    @DisplayName("getById returns empty if not found")
    void testGetByIdNotFound() {
        Mockito.when(listingRepo.findById(1L)).thenReturn(Optional.empty());
        Optional<ListingDto> result = listingService.getById(1L);
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("create saves and returns ListingDto")
    void testCreate() {
        ListingDto dto = new ListingDto();
        Listing listing = new Listing();
        Mockito.when(transformer.fromDto(dto)).thenReturn(listing);
        Mockito.when(listingRepo.save(listing)).thenReturn(listing);
        Mockito.when(transformer.toDto(listing)).thenReturn(dto);
        ListingDto result = listingService.create(dto);
        assertNotNull(result);
    }

    @Test
    @DisplayName("delete returns true if exists, false otherwise")
    void testDelete() {
        Mockito.when(listingRepo.existsById(1L)).thenReturn(true);
        assertTrue(listingService.delete(1L));
        Mockito.when(listingRepo.existsById(2L)).thenReturn(false);
        assertFalse(listingService.delete(2L));
    }
}

