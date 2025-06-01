package com.real.interview.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ListingDto {
    private Long id;
    private String title;
    private String address;
    private BigDecimal price;
    private int bedrooms;
    private int bathrooms;
    private String description;
    private AgentDto agent;
    private List<ClientDto> interestedClients;
    private String propertyType; // e.g., "House", "Apartment", etc.
    private String listingStatus; // e.g., "Available", "Under Offer", "Sold"
    private String listingDate; // Date when the listing was created
    private String lastUpdated; // Date when the listing was last updated
    private String imageUrl; // URL to an image of the property
}