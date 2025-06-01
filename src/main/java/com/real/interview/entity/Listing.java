package com.real.interview.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Listing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String address;
    private Double price;
    private Integer bedrooms;
    private Integer bathrooms;
    private String description;

    @ManyToOne
    @JoinColumn(name = "agent_id")
    private Agent agent;

    @ManyToMany
    @JoinTable(
            name = "listing_client",
            joinColumns = @JoinColumn(name = "listing_id"),
            inverseJoinColumns = @JoinColumn(name = "client_id")
    )
    private List<Client> interestedClients;

}