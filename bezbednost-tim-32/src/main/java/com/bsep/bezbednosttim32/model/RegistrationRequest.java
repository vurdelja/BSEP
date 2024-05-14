package com.bsep.bezbednosttim32.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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


    private boolean approved;
}
