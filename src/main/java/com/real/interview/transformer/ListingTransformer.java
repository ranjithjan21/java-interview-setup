package com.real.interview.transformer;

import com.real.interview.dto.AgentDto;
import com.real.interview.dto.ClientDto;
import com.real.interview.dto.ListingDto;
import com.real.interview.entity.Agent;
import com.real.interview.entity.Client;
import com.real.interview.entity.Listing;
import com.real.interview.repository.AgentRepository;
import com.real.interview.repository.ClientRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ListingTransformer {
    private final AgentRepository agentRepo;
    private final ClientRepository clientRepo;

    public ListingTransformer(AgentRepository agentRepo, ClientRepository clientRepo) {
        this.agentRepo = agentRepo;
        this.clientRepo = clientRepo;
    }

    public ListingDto toDto(Listing listing) {
        ListingDto dto = new ListingDto();
        dto.setId(listing.getId());
        dto.setTitle(listing.getTitle());
        dto.setAddress(listing.getAddress());
        dto.setPrice(listing.getPrice() != null ? java.math.BigDecimal.valueOf(listing.getPrice()) : null);
        dto.setBedrooms(listing.getBedrooms() != null ? listing.getBedrooms() : 0);
        dto.setBathrooms(listing.getBathrooms() != null ? listing.getBathrooms() : 0);
        dto.setDescription(listing.getDescription());
        if (listing.getAgent() != null) {
            dto.setAgent(toAgentDto(listing.getAgent()));
        }
        if (listing.getInterestedClients() != null) {
            dto.setInterestedClients(listing.getInterestedClients().stream().map(this::toClientDto).collect(Collectors.toList()));
        }
        return dto;
    }

    public Listing fromDto(ListingDto dto) {
        Listing listing = new Listing();
        listing.setId(dto.getId());
        listing.setTitle(dto.getTitle());
        listing.setAddress(dto.getAddress());
        listing.setPrice(dto.getPrice() != null ? dto.getPrice().doubleValue() : null);
        listing.setBedrooms(dto.getBedrooms());
        listing.setBathrooms(dto.getBathrooms());
        listing.setDescription(dto.getDescription());
        if (dto.getAgent() != null && dto.getAgent().getId() != null) {
            agentRepo.findById(dto.getAgent().getId()).ifPresent(listing::setAgent);
        }
        if (dto.getInterestedClients() != null) {
            List<Client> clients = dto.getInterestedClients().stream()
                .map(cdto -> clientRepo.findById(cdto.getId()).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
            listing.setInterestedClients(clients);
        }
        return listing;
    }

    public AgentDto toAgentDto(Agent agent) {
        AgentDto dto = new AgentDto();
        dto.setId(agent.getId());
        dto.setName(agent.getName());
        dto.setEmail(agent.getEmail());
        return dto;
    }

    public ClientDto toClientDto(Client client) {
        ClientDto dto = new ClientDto();
        dto.setId(client.getId());
        dto.setName(client.getName());
        dto.setEmail(client.getEmail());
        return dto;
    }
}

