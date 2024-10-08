package com.bsep.bezbednosttim32.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    private String email;
    private String password; //private
    private String captchaToken; // Add this line
    //private String totpCode; // Dodato polje za TOTP kod

}
