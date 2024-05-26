package com.bsep.bezbednosttim32.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "commercials")
public class Commercial {
    @Id
    @GeneratedValue
    private Integer id;

    private String client;
    private String slogan;
    private String duration;
    private String description;
}
