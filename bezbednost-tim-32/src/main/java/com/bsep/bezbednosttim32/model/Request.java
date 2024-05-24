package com.bsep.bezbednosttim32.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "requests")

public class Request {
    @Id
    @GeneratedValue
    private Integer id;

    private String email;
    private String password;
    private String address;
    private String city;
    private String country;
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    private String firstName; // individual
    private String lastName; //individual
    private String companyName; // legal entities
    private String pib; // legal entities

    @Enumerated(EnumType.STRING)
    private PackageType packageType;


    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    private String activationToken;

    @Column
    private LocalDateTime rejectionTime;

}
