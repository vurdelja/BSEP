package com.bsep.bezbednosttim32.auth;

import com.bsep.bezbednosttim32.model.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String email;
    private String password;
    private String address;
    private String city;
    private String country;
    private String phoneNumber;
    private UserType userType;
    private String firstName; // individual
    private String lastName; //individual
    private String companyName; // legal entities
    private String pib;
}
