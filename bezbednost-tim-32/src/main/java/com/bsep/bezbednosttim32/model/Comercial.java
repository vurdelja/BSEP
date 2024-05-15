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
@Table(name = "comercials")

public class Comercial {
    @jakarta.persistence.Id
    @GeneratedValue
    private Integer id;
    private Integer clientId;
    private String slogan;
    private String duration;
    private String description;

}
