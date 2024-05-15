package com.bsep.bezbednosttim32.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "requests")

public class RegistrationRequest {
    @Id
    @GeneratedValue
    private Integer Id;

    private String email;
    private String password;


    @Enumerated(EnumType.STRING)
    private RequestStatus status;
}
