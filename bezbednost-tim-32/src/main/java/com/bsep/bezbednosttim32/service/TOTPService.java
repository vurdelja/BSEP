package com.bsep.bezbednosttim32.service;

import org.jboss.aerogear.security.otp.Totp;

public class TOTPService {

    // Verifies the OTP code
    public boolean verifyCode(String secret, String code) {
        Totp totp = new Totp(secret);
        return totp.verify(code);
    }
}
