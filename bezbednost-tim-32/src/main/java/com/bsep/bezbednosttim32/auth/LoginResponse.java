package com.bsep.bezbednosttim32.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String message;
    private String accessToken;
    private String refreshToken;

    public LoginResponse(String s) {
    }
}
