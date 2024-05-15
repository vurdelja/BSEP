package com.bsep.bezbednosttim32.model;

import jakarta.persistence.*;

import java.util.Date;

public class ActivationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="token_id")

    private Long tokenId;

    @Column(name="confirmation_token")
    private String confirmationToken;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

}

