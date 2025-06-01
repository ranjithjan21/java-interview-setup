package com.real.interview.dto;

import lombok.Data;

@Data
public class ClientDto {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String preferredContactMethod;
    private String preferredLocation;
    private String budgetRange;
    private String propertyTypePreference;
    private String additionalNotes;

}

