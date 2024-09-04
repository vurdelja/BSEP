package com.bsep.bezbednosttim32.service;

import org.jboss.aerogear.security.otp.api.Base32;

public class TOTPManager {

    public static String generateSecretKey() {
        return Base32.random();
    }
}