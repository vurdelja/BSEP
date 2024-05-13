package com.bsep.bezbednosttim32.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    private long id;

    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String country;
    private String phoneNumber;
    private UserType userType;
    private String companyName; // For legal entities
    private String PIB; // PIB for legal entities
}

enum UserType {
    INDIVIDUAL,
    LEGAL_ENTITY
}