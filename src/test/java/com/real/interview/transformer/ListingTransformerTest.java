package com.real.interview.transformer;

import com.real.interview.dto.AgentDto;
import com.real.interview.dto.ClientDto;
import com.real.interview.dto.ListingDto;
import com.real.interview.entity.Agent;
import com.real.interview.entity.Client;
import com.real.interview.entity.Listing;
import com.real.interview.repository.AgentRepository;
import com.real.interview.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ListingTransformerTest {
    @Mock
    private AgentRepository agentRepo;
    @Mock
    private ClientRepository clientRepo;
    private ListingTransformer transformer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        transformer = new ListingTransformer(agentRepo, clientRepo);
    }

    @Test
    @DisplayName("toDto maps Listing to ListingDto")
    void testToDto() {
        Listing listing = new Listing();
        listing.setId(1L);
        listing.setTitle("Test");
        listing.setAddress("Addr");
        listing.setPrice(100.0);
        listing.setBedrooms(2);
        listing.setBathrooms(1);
        listing.setDescription("Desc");
        Agent agent = new Agent();
        agent.setId(10L);
        agent.setName("Agent");
        agent.setEmail("a@a.com");
        listing.setAgent(agent);
        Client client = new Client();
        client.setId(20L);
        client.setName("Client");
        client.setEmail("c@c.com");
        listing.setInterestedClients(List.of(client));
        ListingDto dto = transformer.toDto(listing);
        assertEquals(1L, dto.getId());
        assertEquals("Test", dto.getTitle());
        assertEquals("Addr", dto.getAddress());
        assertEquals(BigDecimal.valueOf(100.0), dto.getPrice());
        assertEquals(2, dto.getBedrooms());
        assertEquals(1, dto.getBathrooms());
        assertEquals("Desc", dto.getDescription());
        assertNotNull(dto.getAgent());
        assertEquals(10L, dto.getAgent().getId());
        assertEquals(1, dto.getInterestedClients().size());
        assertEquals(20L, dto.getInterestedClients().get(0).getId());
    }

    @Test
    @DisplayName("fromDto maps ListingDto to Listing")
    void testFromDto() {
        ListingDto dto = new ListingDto();
        dto.setId(2L);
        dto.setTitle("Test2");
        dto.setAddress("Addr2");
        dto.setPrice(BigDecimal.valueOf(200.0));
        dto.setBedrooms(3);
        dto.setBathrooms(2);
        dto.setDescription("Desc2");
        AgentDto agentDto = new AgentDto();
        agentDto.setId(11L);
        agentDto.setName("Agent2");
        agentDto.setEmail("a2@a.com");
        dto.setAgent(agentDto);
        ClientDto clientDto = new ClientDto();
        clientDto.setId(21L);
        clientDto.setName("Client2");
        clientDto.setEmail("c2@c.com");
        dto.setInterestedClients(List.of(clientDto));
        Agent agent = new Agent();
        agent.setId(11L);
        Mockito.when(agentRepo.findById(11L)).thenReturn(Optional.of(agent));
        Client client = new Client();
        client.setId(21L);
        Mockito.when(clientRepo.findById(21L)).thenReturn(Optional.of(client));
        Listing listing = transformer.fromDto(dto);
        assertEquals(2L, listing.getId());
        assertEquals("Test2", listing.getTitle());
        assertEquals("Addr2", listing.getAddress());
        assertEquals(200.0, listing.getPrice());
        assertEquals(3, listing.getBedrooms());
        assertEquals(2, listing.getBathrooms());
        assertEquals("Desc2", listing.getDescription());
        assertNotNull(listing.getAgent());
        assertEquals(11L, listing.getAgent().getId());
        assertEquals(1, listing.getInterestedClients().size());
        assertEquals(21L, listing.getInterestedClients().get(0).getId());
    }

    @Test
    @DisplayName("toAgentDto maps Agent to AgentDto")
    void testToAgentDto() {
        Agent agent = new Agent();
        agent.setId(100L);
        agent.setName("AgentName");
        agent.setEmail("agent@email.com");
        AgentDto dto = transformer.toAgentDto(agent);
        assertEquals(100L, dto.getId());
        assertEquals("AgentName", dto.getName());
        assertEquals("agent@email.com", dto.getEmail());
    }

    @Test
    @DisplayName("toClientDto maps Client to ClientDto")
    void testToClientDto() {
        Client client = new Client();
        client.setId(200L);
        client.setName("ClientName");
        client.setEmail("client@email.com");
        ClientDto dto = transformer.toClientDto(client);
        assertEquals(200L, dto.getId());
        assertEquals("ClientName", dto.getName());
        assertEquals("client@email.com", dto.getEmail());
    }
}
