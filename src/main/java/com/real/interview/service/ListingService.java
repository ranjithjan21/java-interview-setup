package com.real.interview.service;

import com.real.interview.entity.Listing;
import com.real.interview.entity.Agent;
import com.real.interview.entity.Client;
import com.real.interview.repository.ListingRepository;
import com.real.interview.repository.AgentRepository;
import com.real.interview.repository.ClientRepository;
import com.real.interview.dto.ListingDto;
import com.real.interview.transformer.ListingTransformer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ListingService {
    private final ListingRepository listingRepo;
    private final AgentRepository agentRepo;
    private final ClientRepository clientRepo;
    private final ListingTransformer transformer;

    // Refactored constructor for testability
    public ListingService(ListingRepository listingRepo, AgentRepository agentRepo, ClientRepository clientRepo, ListingTransformer transformer) {
        this.listingRepo = listingRepo;
        this.agentRepo = agentRepo;
        this.clientRepo = clientRepo;
        this.transformer = transformer;
    }

    public List<ListingDto> getAll() {
        return listingRepo.findAll().stream().map(transformer::toDto).collect(Collectors.toList());
    }

    public Optional<ListingDto> getById(Long id) {
        return listingRepo.findById(id).map(transformer::toDto);
    }

    public ListingDto create(ListingDto listingDto) {
        Listing listing = transformer.fromDto(listingDto);
        return transformer.toDto(listingRepo.save(listing));
    }

    public Optional<ListingDto> update(Long id, ListingDto updatedDto) {
        return listingRepo.findById(id).map(listing -> {
            listing.setTitle(updatedDto.getTitle());
            listing.setAddress(updatedDto.getAddress());
            listing.setPrice(updatedDto.getPrice() != null ? updatedDto.getPrice().doubleValue() : null);
            listing.setBedrooms(updatedDto.getBedrooms());
            listing.setBathrooms(updatedDto.getBathrooms());
            listing.setDescription(updatedDto.getDescription());
            if (updatedDto.getAgent() != null && updatedDto.getAgent().getId() != null) {
                agentRepo.findById(updatedDto.getAgent().getId()).ifPresent(listing::setAgent);
            }
            if (updatedDto.getInterestedClients() != null) {
                List<Client> clients = updatedDto.getInterestedClients().stream()
                    .map(cdto -> clientRepo.findById(cdto.getId()).orElse(null))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
                listing.setInterestedClients(clients);
            }
            return transformer.toDto(listingRepo.save(listing));
        });
    }

    public boolean delete(Long id) {
        if (listingRepo.existsById(id)) {
            listingRepo.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<ListingDto> assignAgent(Long listingId, Long agentId) {
        Listing listing = listingRepo.findById(listingId).orElse(null);
        Agent agent = agentRepo.findById(agentId).orElse(null);
        if (listing == null || agent == null) return Optional.empty();
        listing.setAgent(agent);
        return Optional.of(transformer.toDto(listingRepo.save(listing)));
    }

    public Optional<ListingDto> addInterestedClient(Long listingId, Long clientId) {
        Listing listing = listingRepo.findById(listingId).orElse(null);
        Client client = clientRepo.findById(clientId).orElse(null);
        if (listing == null || client == null) return Optional.empty();
        if (!listing.getInterestedClients().contains(client)) {
            listing.getInterestedClients().add(client);
        }
        return Optional.of(transformer.toDto(listingRepo.save(listing)));
    }
}
