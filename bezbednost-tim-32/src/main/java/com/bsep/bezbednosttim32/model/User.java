package com.bsep.bezbednosttim32.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jboss.aerogear.security.otp.api.Base32;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue
    private Integer id;

    private String email;
    private String password;
    private String passwordConfirm;
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
    private Role role;

    private String totpSecret;

    // Novo polje za čuvanje šifrovanog ključa
    @Column(name = "encryption_key", length = 512)
    private String encryptionKey;  // Čuva šifrovani ključ u bazi kao Base64

    // Novo polje za čuvanje IV (inicijalizacionog vektora)
    @Column(name = "iv", length = 64)
    private String iv;  // Čuva IV u bazi kao Base64

    @PrePersist
    public void prePersist() {
        totpSecret = Base32.random(); // Generate and store a TOTP secret when user is created
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

